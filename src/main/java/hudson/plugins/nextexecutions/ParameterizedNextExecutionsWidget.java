package hudson.plugins.nextexecutions;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.View;
import hudson.plugins.nextexecutions.NextBuilds.DescriptorImpl;
import java.util.Collection;
import java.util.List;
import jenkins.model.Jenkins;
import jenkins.widgets.WidgetFactory;
import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.parameterizedscheduler.ParameterizedTimerTrigger;

public class ParameterizedNextExecutionsWidget extends NextExecutionsWidget {

    public ParameterizedNextExecutionsWidget(@NonNull String ownerUrl) {
        super(ownerUrl, ParameterizedTimerTrigger.class);
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

    @Symbol("parameterizedNextExecutionsWidget")
    @Extension(optional = true)
    public static final class FactoryImpl extends WidgetFactory<View, ParameterizedNextExecutionsWidget> {

        @Override
        public Class<View> type() {
            return View.class;
        }

        @Override
        public Class<ParameterizedNextExecutionsWidget> widgetType() {
            return ParameterizedNextExecutionsWidget.class;
        }

        @NonNull
        @Override
        public Collection<ParameterizedNextExecutionsWidget> createFor(@NonNull View target) {
            if (Jenkins.get().getPlugin("parameterized-scheduler") != null) {
                return List.of(new ParameterizedNextExecutionsWidget(target.getUrl()));
            }
            return List.of();
        }
    }
}
