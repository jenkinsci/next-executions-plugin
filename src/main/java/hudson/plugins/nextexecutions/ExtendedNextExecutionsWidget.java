package hudson.plugins.nextexecutions;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.View;
import hudson.plugins.nextexecutions.utils.ExtendedNextExecutionsUtils;
import io.jenkins.plugins.extended_timer_trigger.ExtendedTimerTrigger;
import java.util.Collection;
import java.util.List;
import jenkins.model.Jenkins;
import jenkins.model.ParameterizedJobMixIn;
import jenkins.widgets.WidgetFactory;
import org.jenkinsci.Symbol;

public class ExtendedNextExecutionsWidget extends NextExecutionsWidget {

    public ExtendedNextExecutionsWidget(@NonNull String ownerUrl) {
        super(ownerUrl, ExtendedTimerTrigger.class);
    }

    @Override
    public String getWidgetName() {
        return Messages.ExtendedExec_WidgetName();
    }

    @Override
    public boolean showWidget() {
        Jenkins j = Jenkins.getInstanceOrNull();
        NextBuilds.DescriptorImpl d =
                j != null ? (NextBuilds.DescriptorImpl) (j.getDescriptorOrDie(NextBuilds.class)) : null;

        if (d == null) {
            return false;
        }
        return d.getShowExtendedWidget();
    }

    @Override
    public String getWidgetId() {
        return super.getWidgetId() + "-extended";
    }

    protected NextBuilds getNextBuild(ParameterizedJobMixIn.ParameterizedJob project) {
        return ExtendedNextExecutionsUtils.getNextBuild(project, triggerClass);
    }

    @Symbol("extendedNextExecutionsWidget")
    @Extension(optional = true, ordinal = -20)
    public static final class FactoryImpl extends WidgetFactory<View, ExtendedNextExecutionsWidget> {

        @Override
        public Class<View> type() {
            return View.class;
        }

        @Override
        public Class<ExtendedNextExecutionsWidget> widgetType() {
            return ExtendedNextExecutionsWidget.class;
        }

        @NonNull
        @Override
        public Collection<ExtendedNextExecutionsWidget> createFor(@NonNull View target) {
            if (Jenkins.get().getPlugin("extended-timer-trigger") != null) {
                return List.of(new ExtendedNextExecutionsWidget(target.getUrl()));
            }
            return List.of();
        }
    }
}
