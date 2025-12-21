package hudson.plugins.nextexecutions.utils;

import hudson.plugins.nextexecutions.NextBuilds;
import hudson.scheduler.CronTabList;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import jenkins.model.ParameterizedJobMixIn;
import org.jenkinsci.plugins.parameterizedscheduler.ParameterizedCronTab;
import org.jenkinsci.plugins.parameterizedscheduler.ParameterizedCronTabList;
import org.jenkinsci.plugins.parameterizedscheduler.ParameterizedTimerTrigger;
import org.kohsuke.accmod.restrictions.suppressions.SuppressRestrictedWarnings;

@SuppressWarnings({"rawtypes", "unchecked", "java:S3011"})
public class ParameterizedNextExecutionsUtils {

    private ParameterizedNextExecutionsUtils() {}

    @SuppressRestrictedWarnings(CronTabList.class)
    public static NextBuilds getNextBuild(
            ParameterizedJobMixIn.ParameterizedJob project, Class<? extends Trigger> triggerClass) {
        Calendar cal = null;
        TimeZone timezone = null;

        // Skip all disabled jobs
        if (project.isDisabled()) {
            return null;
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
                        Calendar next = list.next();
                        if (next != null && (cal == null || cal.after(next))) {
                            cal = next;
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
