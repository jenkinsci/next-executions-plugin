package hudson.plugins.nextexecutions.columns;

import hudson.Extension;
import hudson.model.Job;
import hudson.plugins.nextexecutions.*;
import hudson.plugins.nextexecutions.utils.NextExecutionsUtils;
import hudson.triggers.TimerTrigger;
import hudson.triggers.Trigger;
import hudson.views.ListViewColumn;
import hudson.views.ListViewColumnDescriptor;
import jenkins.model.Jenkins;
import jenkins.model.ParameterizedJobMixIn;
import org.kohsuke.stapler.DataBoundConstructor;

@SuppressWarnings("rawtypes")
public class NextExecutionColumn extends ListViewColumn {

    protected Class<? extends Trigger> triggerClass;

    @DataBoundConstructor
    public NextExecutionColumn() {
        triggerClass = TimerTrigger.class;
    }

    public String getColumnId() {
        return "column-next-launch";
    }

    public String getNextExecution(Job job) {
        if (job instanceof ParameterizedJobMixIn.ParameterizedJob project) {
            NextBuilds b = getNextBuild(project);
            if (b != null) {
                return b.getDate();
            }
        }
        return "";
    }

    protected NextBuilds getNextBuild(ParameterizedJobMixIn.ParameterizedJob project) {
        return NextExecutionsUtils.getNextBuild(project, triggerClass);
    }

    public boolean getShowParameterizedWidget() {
        Jenkins j = Jenkins.getInstanceOrNull();
        NextBuilds.DescriptorImpl d =
                j != null ? (NextBuilds.DescriptorImpl) (j.getDescriptorOrDie(NextBuilds.class)) : null;
        if (d == null) {
            return false;
        }
        return d.getShowParameterizedWidget() && j.getPlugin("parameterized-scheduler") != null;
    }

    @Extension
    public static class DescriptorImpl extends ListViewColumnDescriptor {

        @Override
        public String getDisplayName() {
            return Messages.NextExecutions_ColumnName();
        }

        @Override
        public boolean shownByDefault() {
            return false;
        }
    }
}
