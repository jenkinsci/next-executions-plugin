package hudson.plugins.nextexecutions;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.View;
import hudson.plugins.nextexecutions.NextBuilds.DescriptorImpl;
import hudson.triggers.SCMTrigger;
import java.util.Collection;
import java.util.List;
import jenkins.model.Jenkins;
import jenkins.widgets.WidgetFactory;

public class PossibleNextExecutionsWidget extends NextExecutionsWidget {

    public PossibleNextExecutionsWidget(String ownerUrl) {
        super(ownerUrl, SCMTrigger.class);
    }

    @Override
    public String getWidgetName() {
        return Messages.PossibleNextExec_WidgetName();
    }

    @Override
    public boolean showWidget() {
        Jenkins j = Jenkins.getInstanceOrNull();
        DescriptorImpl d = j != null ? (DescriptorImpl) (j.getDescriptorOrDie(NextBuilds.class)) : null;

        if (d == null) {
            return false;
        }
        return d.getShowPossibleWidget();
    }

    @Override
    public String getWidgetId() {
        return super.getWidgetId() + "-possible";
    }

    @Extension
    public static final class FactoryImpl extends WidgetFactory<View, PossibleNextExecutionsWidget> {

        @Override
        public Class<View> type() {
            return View.class;
        }

        @Override
        public Class<PossibleNextExecutionsWidget> widgetType() {
            return PossibleNextExecutionsWidget.class;
        }

        @NonNull
        @Override
        public Collection<PossibleNextExecutionsWidget> createFor(@NonNull View target) {
            return List.of(new PossibleNextExecutionsWidget(target.getUrl()));
        }
    }
}
