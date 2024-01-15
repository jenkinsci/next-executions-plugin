package hudson.plugins.nextexecutions;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Queue;
import hudson.model.Queue.Item;
import hudson.model.Queue.WaitingItem;
import hudson.model.TopLevelItem;
import hudson.model.View;
import hudson.plugins.nextexecutions.NextBuilds.DescriptorImpl;
import hudson.plugins.nextexecutions.utils.NextExecutionsUtils;
import hudson.plugins.nextexecutions.utils.ParameterizedNextExecutionsUtils;
import hudson.triggers.TimerTrigger;
import hudson.triggers.Trigger;
import hudson.widgets.Widget;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import jenkins.model.Jenkins;
import jenkins.model.ParameterizedJobMixIn;
import jenkins.widgets.WidgetFactory;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

/**
 * Widget in the main sidebar with a list
 * of projects and their next scheduled
 * build's date. The list is sorted by date.
 *
 * It includes those in the Queue with an execution
 * delay of more than a minute.
 *
 * @author ialbors
 *
 */
@ExportedBean
@SuppressWarnings("rawtypes")
public class NextExecutionsWidget extends Widget {

    @NonNull
    private final String ownerUrl;

    private Class<? extends Trigger> triggerClass;

    public NextExecutionsWidget(@NonNull String ownerUrl) {
        this(ownerUrl, TimerTrigger.class);
    }

    public NextExecutionsWidget(@NonNull String ownerUrl, Class<? extends Trigger> triggerClass) {
        this.ownerUrl = ownerUrl;
        this.triggerClass = triggerClass;
    }

    @Override
    public String getOwnerUrl() {
        return ownerUrl;
    }

    @Exported(name = "next_executions")
    public List<NextBuilds> getBuilds() {
        List<NextBuilds> nblist = new Vector<NextBuilds>();

        List<ParameterizedJobMixIn.ParameterizedJob> l;

        View v = Stapler.getCurrentRequest().findAncestorObject(View.class);

        Jenkins j = Jenkins.getInstanceOrNull();

        if (j == null) {
            return null;
        }

        DescriptorImpl d = (DescriptorImpl) (j.getDescriptorOrDie(NextBuilds.class));

        if (d.getFilterByView() && v != null) {
            Collection<TopLevelItem> tli = v.getItems();
            Vector<ParameterizedJobMixIn.ParameterizedJob> vector =
                    new Vector<ParameterizedJobMixIn.ParameterizedJob>();
            for (TopLevelItem topLevelItem : tli) {
                if (topLevelItem instanceof ParameterizedJobMixIn.ParameterizedJob) {
                    vector.add((ParameterizedJobMixIn.ParameterizedJob) topLevelItem);
                }
            }
            l = vector;
        } else {
            l = j.getAllItems(ParameterizedJobMixIn.ParameterizedJob.class);
        }

        for (ParameterizedJobMixIn.ParameterizedJob project : l) {
            NextBuilds nb = NextExecutionsUtils.getNextBuild(project, triggerClass);
            if (nb != null) {
                nblist.add(nb);
            }
            // Check parameterized
            else if (getShowParameterizedWidget()) {
                nb = ParameterizedNextExecutionsUtils.getNextBuild(project, triggerClass);
                if (nb != null) {
                    nblist.add(nb);
                }
            }
        }

        // Get also the items in the queue but only those that have a waiting
        // period of more than a minute.

        // Only for this class. Not its children
        if (this.getClass() == NextExecutionsWidget.class) {
            Item[] queueItems = Queue.getInstance().getItems();
            for (Item item : queueItems) {
                if (item instanceof WaitingItem && item.task instanceof ParameterizedJobMixIn.ParameterizedJob) {
                    WaitingItem waitingItem = (WaitingItem) item;
                    Calendar now = Calendar.getInstance();
                    long nowMilliseconds = now.getTimeInMillis();
                    now.setTimeInMillis(nowMilliseconds + 60 * 1000);
                    if (waitingItem.timestamp.after(now)) {
                        NextBuilds nb = new NextBuilds(
                                (ParameterizedJobMixIn.ParameterizedJob) item.task, waitingItem.timestamp);
                        nblist.add(nb);
                    }
                }
            }
        }

        Collections.sort(nblist);
        return nblist;
    }

    public String getWidgetName() {
        return Messages.NextExec_WidgetName();
    }

    public String getWidgetId() {
        return "next-exec";
    }

    public boolean showWidget() {
        return true;
    }

    public boolean getShowParameterizedWidget() {
        Jenkins j = Jenkins.getInstanceOrNull();
        DescriptorImpl d = j != null ? (DescriptorImpl) (j.getDescriptorOrDie(NextBuilds.class)) : null;
        if (d == null) {
            return false;
        }
        return d.getShowParameterizedWidget();
    }

    @Symbol("nextExecutionsWidget")
    @Extension
    public static final class FactoryImpl extends WidgetFactory<View, NextExecutionsWidget> {
        @Override
        public Class<View> type() {
            return View.class;
        }

        @Override
        public Class<NextExecutionsWidget> widgetType() {
            return NextExecutionsWidget.class;
        }

        @NonNull
        @Override
        public Collection<NextExecutionsWidget> createFor(@NonNull View target) {
            return List.of(new NextExecutionsWidget(target.getUrl()));
        }
    }
}
