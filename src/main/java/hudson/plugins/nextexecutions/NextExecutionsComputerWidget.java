package hudson.plugins.nextexecutions;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Computer;
import hudson.plugins.nextexecutions.utils.NextExecutionsUtils;
import hudson.triggers.TimerTrigger;
import hudson.widgets.Widget;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import jenkins.widgets.WidgetFactory;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

/**
 * Widget in the {@link Computer} page's sidebar with a list
 * of projects (tied to that Computer) and their next scheduled
 * build's date. The list is sorted by date.
 *
 * @author ialbors
 *
 */
@ExportedBean
@SuppressWarnings("rawtypes")
public class NextExecutionsComputerWidget extends Widget {

    @NonNull
    private final Computer computer;

    public NextExecutionsComputerWidget(@NonNull Computer computer) {
        this.computer = computer;
    }

    public String getWidgetName() {
        return Messages.NextExecComputer_WidgetName();
    }

    public String getWidgetId() {
        return "next-exec-computer";
    }

    @Override
    public String getOwnerUrl() {
        return computer.getUrl();
    }

    @Exported(name = "next_executions_computer")
    public List<NextBuilds> getBuilds() {
        List<NextBuilds> nblist = new Vector<NextBuilds>();

        for (AbstractProject project : computer.getTiedJobs()) {
            NextBuilds nb = NextExecutionsUtils.getNextBuild(project, TimerTrigger.class);
            if (nb != null) {
                nblist.add(nb);
            }
        }
        Collections.sort(nblist);
        return nblist;
    }

    @Symbol("nextExecutionsComputerWidget")
    @Extension
    public static final class FactoryImpl extends WidgetFactory<Computer, NextExecutionsComputerWidget> {
        @Override
        public Class<Computer> type() {
            return Computer.class;
        }

        @Override
        public Class<NextExecutionsComputerWidget> widgetType() {
            return NextExecutionsComputerWidget.class;
        }

        @NonNull
        @Override
        public Collection<NextExecutionsComputerWidget> createFor(@NonNull Computer target) {
            return List.of(new NextExecutionsComputerWidget(target));
        }
    }
}
