package hudson.plugins.nextexecutions.utils;

import hudson.model.AbstractProject;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import hudson.plugins.nextexecutions.NextBuilds;
import hudson.scheduler.CronTab;
import hudson.scheduler.CronTabList;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import jenkins.model.ParameterizedJobMixIn;

@SuppressWarnings({"rawtypes", "unchecked", "java:S3011"})
public class NextExecutionsUtils {

    private NextExecutionsUtils() {}

    public static NextBuilds getNextBuild(ParameterizedJobMixIn.ParameterizedJob project, Class<? extends Trigger> triggerClass) {
        Calendar cal = null;
        TimeZone timezone = null;
        // Only AbstractProject has isDisabled method
        if ((project instanceof AbstractProject && !((AbstractProject) project).isDisabled())
                || !(project instanceof AbstractProject)) {
            Map<TriggerDescriptor, Trigger<?>> triggers = project.getTriggers();
            Iterator<Map.Entry<TriggerDescriptor, Trigger<?>>> iterator = triggers.entrySet().iterator();
            while (iterator.hasNext()) {
                Trigger trigger = iterator.next().getValue();
                if (trigger.getClass().equals(triggerClass)) {
                    try {
                        Field triggerTabsField = Trigger.class.getDeclaredField("tabs");
                        triggerTabsField.setAccessible(true);

                        CronTabList cronTabList = (CronTabList) triggerTabsField.get(trigger);

                        Field crontablistTabsField = CronTabList.class.getDeclaredField("tabs");
                        crontablistTabsField.setAccessible(true);

                        List<CronTab> crons = (Vector<CronTab>) crontablistTabsField.get(cronTabList);

                        for (CronTab cronTab : crons) {
                            timezone = cronTab.getTimeZone() != null ? cronTab.getTimeZone() : TimeZone.getDefault();
                            Calendar now = new GregorianCalendar(timezone);
                            cal = (cal == null || cal.compareTo(cronTab.ceil(now)) > 0) ? cronTab.ceil(now) : cal;
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        // Do nothing
                    }
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
