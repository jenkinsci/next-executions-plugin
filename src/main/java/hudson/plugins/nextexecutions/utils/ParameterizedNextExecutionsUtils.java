package hudson.plugins.nextexecutions.utils;

import hudson.plugins.nextexecutions.NextBuilds;
import hudson.scheduler.CronTabList;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import jenkins.model.ParameterizedJobMixIn;
import org.jenkinsci.plugins.parameterizedscheduler.ParameterizedCronTab;
import org.jenkinsci.plugins.parameterizedscheduler.ParameterizedCronTabList;
import org.jenkinsci.plugins.parameterizedscheduler.ParameterizedTimerTrigger;
import org.kohsuke.accmod.restrictions.suppressions.SuppressRestrictedWarnings;

@SuppressWarnings({"rawtypes", "java:S3011"})
public class ParameterizedNextExecutionsUtils {

    private ParameterizedNextExecutionsUtils() {}

    @SuppressRestrictedWarnings(CronTabList.class)
    public static NextBuilds getNextBuild(
            ParameterizedJobMixIn.ParameterizedJob project, Class<? extends Trigger> triggerClass) {
        Calendar cal = null;
        // TimeZone timezone = null;
        String paramsTooltip = null;

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
                ParameterizedTimerTrigger ptt = (ParameterizedTimerTrigger) trigger;
                ParameterizedCronTabList cronTabList = ptt.getCronTabList();
                if (cronTabList != null) {
                    ParameterizedCronTab nextTab = cronTabList.nextParameterizedCronTab();
                    if (nextTab != null) {
                        Calendar next = nextTab.next();
                        if (next != null && (cal == null || cal.after(next))) {
                            cal = next;
                            Map<String, String> params = nextTab.getParameterValues();
                            if (params != null && !params.isEmpty()) {
                                StringBuilder sb = new StringBuilder();
                                for (Map.Entry<String, String> entry : params.entrySet()) {
                                    if (sb.length() > 0) sb.append(", ");
                                    sb.append(entry.getKey()).append("=").append(entry.getValue());
                                }
                                paramsTooltip = sb.toString();
                            }
                        }
                    }
                }
            }
        }
        if (cal != null) {
            return new NextBuilds(project, cal, paramsTooltip);
        } else {
            return null;
        }
    }
}
