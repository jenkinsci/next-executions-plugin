package hudson.plugins.nextexecutions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import hudson.model.FreeStyleProject;
import hudson.triggers.TimerTrigger;
import hudson.util.FormValidation;
import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;
import org.kohsuke.stapler.StaplerRequest2;

@WithJenkins
class NextBuildsTest {

    @Test
    @Disabled("Flaky test")
    void testGetCorrectDate(JenkinsRule j) throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("test");
        project.addTrigger(new TimerTrigger("@daily"));
        ZonedDateTime zdt = ZonedDateTime.now();
        NextBuilds.DescriptorImpl descriptor = j.jenkins.getDescriptorByType(NextBuilds.DescriptorImpl.class);
        String format = descriptor.getDateFormat();
        if (format == null) {
            format = descriptor.getDefault();
        }
        NextBuilds nextBuilds = new NextBuilds(project, Calendar.getInstance());
        assertEquals(zdt.format(DateTimeFormatter.ofPattern(format)), nextBuilds.getDate());
        descriptor.setDateFormat("yyyy-MM-dd HH:mm:ss");
        format = descriptor.getDateFormat();
        assertEquals(zdt.format(DateTimeFormatter.ofPattern(format)), nextBuilds.getDate());
        j.jenkins.getQueue().clear();
    }

    @Test
    void testGetName(JenkinsRule j) throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("test");
        NextBuilds nextBuilds1 = new NextBuilds(project, Calendar.getInstance());
        assertEquals("test", nextBuilds1.getName());
        assertEquals("test", nextBuilds1.getshortName());
        project.setDisplayName("Freestyle Project using Maven TEST");
        NextBuilds nextBuilds2 = new NextBuilds(project, Calendar.getInstance());
        assertEquals("Freestyle Project using Maven TEST", nextBuilds2.getName());
        assertEquals("Freestyle Project u...", nextBuilds2.getshortName());
        j.jenkins.getQueue().clear();
    }

    @Test
    void testCompareToWithEqualDates(JenkinsRule j) throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("test");
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        date.set(2024, Calendar.MARCH, 19);
        NextBuilds nextBuild1 = new NextBuilds(project, date);
        NextBuilds nextBuild2 = new NextBuilds(project, date);
        assertEquals(0, nextBuild1.compareTo(nextBuild2));
        assertEquals(0, nextBuild1.compareTo("Not an Instance"));
    }

    @Test
    void testCompareToWithLesserDate(JenkinsRule j) throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("test");
        Calendar date1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        date1.set(2024, Calendar.MARCH, 18);
        Calendar date2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        date2.set(2024, Calendar.MARCH, 19);
        NextBuilds nextBuild1 = new NextBuilds(project, date1);
        NextBuilds nextBuild2 = new NextBuilds(project, date2);
        assertTrue(nextBuild1.compareTo(nextBuild2) < 0);
    }

    @Test
    void testCompareToWithGreaterDate(JenkinsRule j) throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("test");
        Calendar date1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        date1.set(2024, Calendar.MARCH, 20);
        Calendar date2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        date2.set(2024, Calendar.MARCH, 19);
        NextBuilds nextBuild1 = new NextBuilds(project, date1);
        NextBuilds nextBuild2 = new NextBuilds(project, date2);
        assertTrue(nextBuild1.compareTo(nextBuild2) > 0);
    }

    @Test
    void testGetUrl(JenkinsRule j) throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("test");
        NextBuilds nextBuilds = new NextBuilds(project, Calendar.getInstance());
        assertNotNull(nextBuilds.getUrl());
    }

    @Test
    void testGetTimeToGoWithCurrentTime(JenkinsRule j) throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("test");
        NextBuilds nextBuilds = new NextBuilds(project, Calendar.getInstance());
        assertEquals("(in )", nextBuilds.getTimeToGo());
    }

    @Test
    void testGetTimeToGoWithFutureTime(JenkinsRule j) throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("test");
        Calendar futureDate = Calendar.getInstance();
        futureDate.add(Calendar.DAY_OF_MONTH, 2);

        NextBuilds nextBuilds = new NextBuilds(project, futureDate);
        assertEquals("(in 1d 23h 59m)", nextBuilds.getTimeToGo());

        futureDate.add(Calendar.HOUR_OF_DAY, 3);
        nextBuilds = new NextBuilds(project, futureDate);
        assertEquals("(in 2d 2h 59m)", nextBuilds.getTimeToGo());

        futureDate.add(Calendar.MINUTE, 15);
        nextBuilds = new NextBuilds(project, futureDate);
        assertEquals("(in 2d 3h 14m)", nextBuilds.getTimeToGo());

        futureDate = Calendar.getInstance();
        futureDate.add(Calendar.HOUR_OF_DAY, 1);
        nextBuilds = new NextBuilds(project, futureDate);
        assertEquals("(in 59m)", nextBuilds.getTimeToGo());

        futureDate.add(Calendar.MINUTE, 30);
        nextBuilds = new NextBuilds(project, futureDate);
        assertEquals("(in 1h 29m)", nextBuilds.getTimeToGo());
    }

    @Test
    void testSetAndGetFilterByView(JenkinsRule j) throws Exception {
        NextBuilds.DescriptorImpl descriptor = new NextBuilds.DescriptorImpl();
        descriptor.setFilterByView(true);
        assertTrue(descriptor.getFilterByView());
        Field field = NextBuilds.DescriptorImpl.class.getDeclaredField("filterByView");
        field.setAccessible(true);
        field.set(descriptor, null);
        assertTrue(descriptor.getFilterByView());
    }

    @Test
    void testSetAndGetShowPossibleWidget(JenkinsRule j) throws Exception {
        NextBuilds.DescriptorImpl descriptor = new NextBuilds.DescriptorImpl();
        descriptor.setShowPossibleWidget(true);
        assertTrue(descriptor.getShowPossibleWidget());
        Field field = NextBuilds.DescriptorImpl.class.getDeclaredField("showPossibleWidget");
        field.setAccessible(true);
        field.set(descriptor, null);
        assertFalse(descriptor.getShowPossibleWidget());
    }

    @Test
    void testSetAndGetShowParameterizedWidget(JenkinsRule j) throws Exception {
        NextBuilds.DescriptorImpl descriptor = new NextBuilds.DescriptorImpl();
        descriptor.setShowParameterizedWidget(true);
        assertTrue(descriptor.getShowParameterizedWidget());
        Field field = NextBuilds.DescriptorImpl.class.getDeclaredField("showParameterizedWidget");
        field.setAccessible(true);
        field.set(descriptor, null);
        assertFalse(descriptor.getShowParameterizedWidget());
    }

    @Test
    void testConfigure(JenkinsRule j) throws Exception {
        StaplerRequest2 staplerRequest = mock(StaplerRequest2.class);
        JSONObject jsonMock = mock(JSONObject.class);
        when(jsonMock.getString("dateFormat")).thenReturn("dd-MM-yyyy");
        when(jsonMock.getBoolean("filterByView")).thenReturn(true);
        when(jsonMock.getBoolean("showPossibleWidget")).thenReturn(true);
        when(jsonMock.getBoolean("showParameterizedWidget")).thenReturn(false);

        NextBuilds.DescriptorImpl descriptor = new NextBuilds.DescriptorImpl();
        boolean result = descriptor.configure(staplerRequest, jsonMock);
        assertTrue(result);
        assertEquals("dd-MM-yyyy", descriptor.getDateFormat());
        assertTrue(descriptor.getFilterByView());
        assertTrue(descriptor.getShowPossibleWidget());
        assertFalse(descriptor.getShowParameterizedWidget());
    }

    @Test
    void testValidDateFormat(JenkinsRule j) {
        String validDateFormat = "yyyy-MM-dd";
        NextBuilds.DescriptorImpl descriptor = new NextBuilds.DescriptorImpl();
        FormValidation result = descriptor.doCheckDateFormat(validDateFormat);
        assertEquals(FormValidation.Kind.OK, result.kind);
    }

    @Test
    void testInvalidDateFormat(JenkinsRule j) {
        String invalidDateFormat = "invalid_format";
        NextBuilds.DescriptorImpl descriptor = new NextBuilds.DescriptorImpl();
        FormValidation result = descriptor.doCheckDateFormat(invalidDateFormat);
        assertEquals(FormValidation.Kind.ERROR, result.kind);
    }
}
