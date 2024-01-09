package hudson.plugins.nextexecutions;

import static org.junit.Assert.*;

import hudson.model.FreeStyleProject;
import hudson.triggers.TimerTrigger;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class NextBuildsTest {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void testGetCorrectDate() {
        try {
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
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void testGetName() {
        try {
            FreeStyleProject project = j.createFreeStyleProject("test");
            NextBuilds nextBuilds1 = new NextBuilds(project, Calendar.getInstance());
            assertEquals("test", nextBuilds1.getName());
            assertEquals("test", nextBuilds1.getshortName());
            project.setDisplayName("Freestyle Project using Maven TEST");
            NextBuilds nextBuilds2 = new NextBuilds(project, Calendar.getInstance());
            assertEquals("Freestyle Project using Maven TEST", nextBuilds2.getName());
            assertEquals("Freestyle Project u...", nextBuilds2.getshortName());
            j.jenkins.getQueue().clear();
        } catch (Exception e) {
            fail(e.toString());
        }
    }
}
