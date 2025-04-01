package hudson.plugins.nextexecutions.utils;

import hudson.plugins.nextexecutions.NextBuilds;
import hudson.scheduler.CronTab;
import hudson.scheduler.CronTabList;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Vector;
import jenkins.model.ParameterizedJobMixIn;
import org.jenkinsci.plugins.parameterizedscheduler.ParameterizedCronTab;
import org.jenkinsci.plugins.parameterizedscheduler.ParameterizedCronTabList;
import org.jenkinsci.plugins.parameterizedscheduler.ParameterizedTimerTrigger;

@SuppressWarnings({"rawtypes", "unchecked", "java:S3011"})
public class ParameterizedNextExecutionsUtils {

    private ParameterizedNextExecutionsUtils() {}

    public static NextBuilds getNextBuild(
            ParameterizedJobMixIn.ParameterizedJob project, Class<? extends Trigger> triggerClass) {
        Calendar cal = null;
        TimeZone timezone = null;

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

        Map<TriggerDescriptor, Trigger<?>> triggers = project.getTriggers();
        Iterator<Map.Entry<TriggerDescriptor, Trigger<?>>> iterator =
                triggers.entrySet().iterator();
        while (iterator.hasNext()) {
            Trigger trigger = iterator.next().getValue();
            if (trigger.getClass().equals(triggerClass) && triggerClass.equals(ParameterizedTimerTrigger.class)) {
                try {
                    Field triggerTabsField = ParameterizedTimerTrigger.class.getDeclaredField("cronTabList");
                    triggerTabsField.setAccessible(true);

                    ParameterizedCronTabList parameterizedCronTabList =
                            (ParameterizedCronTabList) triggerTabsField.get(trigger);

                    Field crontablistTabsField = ParameterizedCronTabList.class.getDeclaredField("cronTabs");
                    crontablistTabsField.setAccessible(true);
                    List<ParameterizedCronTab> parameterizedCrons =
                            (ArrayList<ParameterizedCronTab>) crontablistTabsField.get(parameterizedCronTabList);

                    for (ParameterizedCronTab parameterizedCron : parameterizedCrons) {
                        Field crontablistField = ParameterizedCronTab.class.getDeclaredField("cronTabList");
                        crontablistField.setAccessible(true);
                        CronTabList list = (CronTabList) crontablistField.get(parameterizedCron);
                        Field crontablistTabsField1 = CronTabList.class.getDeclaredField("tabs");
                        crontablistTabsField1.setAccessible(true);
                        List<CronTab> crons = (Vector<CronTab>) crontablistTabsField1.get(list);
                        for (CronTab cronTab : crons) {
                            timezone = cronTab.getTimeZone() != null ? cronTab.getTimeZone() : TimeZone.getDefault();
                            Calendar now = new GregorianCalendar(timezone);
                            cal = (cal == null || cal.compareTo(cronTab.ceil(now)) > 0) ? cronTab.ceil(now) : cal;
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
