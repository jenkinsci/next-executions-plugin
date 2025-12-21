package hudson.plugins.nextexecutions.utils;

import hudson.plugins.nextexecutions.NextBuilds;
import hudson.scheduler.CronTabList;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import io.jenkins.plugins.extended_timer_trigger.ExtendedTimerTrigger;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import jenkins.model.ParameterizedJobMixIn;
import org.kohsuke.accmod.restrictions.suppressions.SuppressRestrictedWarnings;

@SuppressWarnings({"rawtypes", "unchecked", "java:S3011"})
public class ExtendedNextExecutionsUtils {

    private ExtendedNextExecutionsUtils() {}

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
            if (trigger instanceof ExtendedTimerTrigger ett && triggerClass.equals(ExtendedTimerTrigger.class)) {
                ZonedDateTime next = ett.getExtendedCronTabList().next();
                if (next != null) {
                    Calendar triggerCal = GregorianCalendar.from(next);
                    if (cal == null || triggerCal.before(cal)) {
                        cal = triggerCal;
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
