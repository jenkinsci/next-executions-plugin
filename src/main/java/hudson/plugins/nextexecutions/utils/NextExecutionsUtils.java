package hudson.plugins.nextexecutions.utils;

import hudson.plugins.nextexecutions.NextBuilds;
import hudson.scheduler.CronTab;
import hudson.scheduler.CronTabList;
import hudson.scheduler.RareOrImpossibleDateException;
import hudson.triggers.Trigger;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;
import jenkins.model.ParameterizedJobMixIn;

@SuppressWarnings({"rawtypes", "unchecked", "java:S3011"})
public class NextExecutionsUtils {

    private NextExecutionsUtils() {}

    public static NextBuilds getNextBuild(
            ParameterizedJobMixIn.ParameterizedJob project, Class<? extends Trigger> triggerClass) {
        Calendar cal = null;

        // Skip all disabled jobs
        try {
            Method isDisabledMethod = project.getClass().getMethod("isDisabled");
            isDisabledMethod.setAccessible(true);
            if ((Boolean) isDisabledMethod.invoke(project)) {
                return null;
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Do nothing
        }

        for (Trigger<?> trigger : project.getTriggers().values()) {
            if (trigger.getClass().equals(triggerClass)) {
                try {
                    Field triggerTabsField = Trigger.class.getDeclaredField("tabs");
                    triggerTabsField.setAccessible(true);

                    CronTabList cronTabList = (CronTabList) triggerTabsField.get(trigger);

                    Field crontablistTabsField = CronTabList.class.getDeclaredField("tabs");
                    crontablistTabsField.setAccessible(true);

                    List<CronTab> crons = (Vector<CronTab>) crontablistTabsField.get(cronTabList);

                    for (CronTab cronTab : crons) {
                        TimeZone timezone =
                                cronTab.getTimeZone() != null ? cronTab.getTimeZone() : TimeZone.getDefault();
                        try {
                            Calendar next = cronTab.ceil(new GregorianCalendar(timezone));
                            if (cal == null || cal.compareTo(next) > 0) {
                                cal = next;
                            }
                        } catch (RareOrImpossibleDateException ignored) {
                            // Ignore this crontab because its next date won't occur in the next two years
                        }
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
