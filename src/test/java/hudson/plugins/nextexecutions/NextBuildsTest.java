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
            j.jenkins.getQueue().clear();
        } catch (Exception e) {
            fail(e.toString());
        }
    }
}
