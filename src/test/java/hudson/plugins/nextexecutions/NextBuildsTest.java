package hudson.plugins.nextexecutions;

import static org.junit.Assert.assertEquals;

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
    public void testGetCorrectNextExecutionTime() {
        try {
            FreeStyleProject project = j.createFreeStyleProject("test");
            project.addTrigger(new TimerTrigger("@daily"));
            ZonedDateTime zdt = ZonedDateTime.now().plusDays(1);
            String format = j.jenkins
                    .getDescriptorByType(NextBuilds.DescriptorImpl.class)
                    .getDateFormat();
            NextBuilds nextBuilds = new NextBuilds(project, Calendar.getInstance());
            assertEquals(
                    nextBuilds.getDate(), DateTimeFormatter.ofPattern(format).format(zdt));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
