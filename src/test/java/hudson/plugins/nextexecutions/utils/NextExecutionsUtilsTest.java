package hudson.plugins.nextexecutions.utils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hudson.model.FreeStyleProject;
import hudson.plugins.nextexecutions.NextBuilds;
import hudson.triggers.TimerTrigger;
import hudson.triggers.Trigger;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import jenkins.model.ParameterizedJobMixIn;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class NextExecutionsUtilsTest {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void testGetNextBuildWhenProjectIsDisabled() {
        try {
            ParameterizedJobMixIn.ParameterizedJob project = mock(ParameterizedJobMixIn.ParameterizedJob.class);
            when(project.isDisabled()).thenReturn(true);

            assertNull(NextExecutionsUtils.getNextBuild(project, Trigger.class));
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void testGetNextBuildWhenProjectHasNoTriggers() {
        try {
            ParameterizedJobMixIn.ParameterizedJob project = mock(ParameterizedJobMixIn.ParameterizedJob.class);
            when(project.isDisabled()).thenReturn(false);
            when(project.getTriggers()).thenReturn(Collections.emptyMap());

            assertNull(NextExecutionsUtils.getNextBuild(project, Trigger.class));
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void testGetNextBuildWhenProjectHasTrigger() {
        try {
            FreeStyleProject project = j.createFreeStyleProject("test");
            Trigger trigger = new TimerTrigger("@daily");
            project.addTrigger(trigger);
            ZonedDateTime zdt = ZonedDateTime.now().plusDays(1).truncatedTo(ChronoUnit.DAYS);
            NextBuilds.DescriptorImpl descriptor = j.jenkins.getDescriptorByType(NextBuilds.DescriptorImpl.class);
            String format = descriptor.getDateFormat();
            if (format == null) {
                format = descriptor.getDefault();
            }
            NextBuilds nb = NextExecutionsUtils.getNextBuild(project, trigger.getClass());
            assertEquals(zdt.format(DateTimeFormatter.ofPattern(format)), nb.getDate());
        } catch (Exception e) {
            fail(e.toString());
        }
    }
}
