package hudson.plugins.nextexecutions.utils;

import hudson.plugins.nextexecutions.NextBuilds;
import hudson.scheduler.CronTabList;
import hudson.scheduler.RareOrImpossibleDateException;
import hudson.triggers.Trigger;
import java.lang.reflect.Field;
import java.util.Calendar;
import jenkins.model.ParameterizedJobMixIn;
import org.kohsuke.accmod.restrictions.suppressions.SuppressRestrictedWarnings;

@SuppressWarnings({"rawtypes", "unchecked", "java:S3011"})
public class NextExecutionsUtils {

    private NextExecutionsUtils() {}

    @SuppressRestrictedWarnings(CronTabList.class)
    public static NextBuilds getNextBuild(
            ParameterizedJobMixIn.ParameterizedJob project, Class<? extends Trigger> triggerClass) {
        Calendar cal = null;

        // Skip all disabled jobs
        if (project.isDisabled()) {
            return null;
        }

        for (Trigger<?> trigger : project.getTriggers().values()) {
            if (trigger.getClass().equals(triggerClass)) {
                try {
                    Field triggerTabsField = Trigger.class.getDeclaredField("tabs");
                    triggerTabsField.setAccessible(true);

                    CronTabList cronTabList = (CronTabList) triggerTabsField.get(trigger);

                    try {
                        cal = cronTabList.next();
                    } catch (RareOrImpossibleDateException ignored) {
                        // Ignore this crontab because its next date won't occur in the next two years
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    // Do nothing
                }
            }
        }
        if (cal != null) {
            return new NextBuilds(project, cal);
        } else {
            return null;
        }
    }
}
