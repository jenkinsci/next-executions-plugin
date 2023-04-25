package hudson.plugins.nextexecutions;

import hudson.Extension;
import hudson.plugins.nextexecutions.NextBuilds.DescriptorImpl;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.parameterizedscheduler.ParameterizedTimerTrigger;

@Extension(optional = true)
public class ParameterizedNextExecutionsWidget extends NextExecutionsWidget {

    public ParameterizedNextExecutionsWidget() {
        triggerClass = ParameterizedTimerTrigger.class;
    }

    @Override
    public String getWidgetName() {
        return Messages.ParameterizedExec_WidgetName();
    }

    @Override
    public boolean showWidget() {
        Jenkins j = Jenkins.getInstanceOrNull();
        DescriptorImpl d = j != null ? (DescriptorImpl) (j.getDescriptorOrDie(NextBuilds.class)) : null;

        if (d == null) {
            return false;
        }
        return d.getShowParameterizedWidget();
    }

    @Override
    public String getWidgetId() {
        return super.getWidgetId() + "-parameterized";
    }
}
