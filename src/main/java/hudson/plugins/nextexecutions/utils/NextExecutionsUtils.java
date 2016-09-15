package hudson.plugins.nextexecutions.utils;

import hudson.model.AbstractProject;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import hudson.plugins.nextexecutions.NextBuilds;
import hudson.scheduler.CronTab;
import hudson.scheduler.CronTabList;
import hudson.triggers.TimerTrigger;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import java.util.Iterator;
import java.util.Map;
import jenkins.model.ParameterizedJobMixIn;

public class NextExecutionsUtils {

    /**
     * Returns the {@link NextBuilds} for the project.
     *
     * @return The {@link NextBuilds} object with the associated next execution
     * date or null.
     */
    @Deprecated
    public static NextBuilds getNextBuild(ParameterizedJobMixIn.ParameterizedJob project) {
        return getNextBuild(project, TimerTrigger.class);
    }

    public static NextBuilds getNextBuild(ParameterizedJobMixIn.ParameterizedJob project, Class<? extends Trigger> triggerClass) {
        Calendar cal = null;
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
                            Date d = new Date();
                            cal = (cal == null || cal.compareTo(cronTab.ceil(d.getTime())) > 0) ? cronTab.ceil(d.getTime()) : cal;
                        }
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
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
