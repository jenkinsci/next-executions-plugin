package hudson.plugins.nextexecutions.columns;

import hudson.model.Job;
import hudson.plugins.nextexecutions.NextBuilds;
import hudson.triggers.Trigger;
import hudson.views.ListViewColumn;
import hudson.views.ListViewColumnDescriptor;
import jenkins.model.ParameterizedJobMixIn;

@SuppressWarnings("rawtypes")
public abstract class NextExecutionColumnBase extends ListViewColumn {

    protected Class<? extends Trigger> triggerClass;

    protected NextExecutionColumnBase() {}

    public abstract String getColumnId();

    public String getNextExecution(Job job) {
        if (job instanceof ParameterizedJobMixIn.ParameterizedJob project) {
            NextBuilds b = getNextBuild(project);
            if (b != null) {
                return b.getDate();
            }
        }
        return "";
    }

    public NextBuilds getNextExecutionInfo(Job job) {
        if (job instanceof ParameterizedJobMixIn.ParameterizedJob project) {
            return getNextBuild(project);
        }
        return null;
    }

    protected abstract NextBuilds getNextBuild(ParameterizedJobMixIn.ParameterizedJob project);

    public abstract static class DescriptorImpl extends ListViewColumnDescriptor {
        @Override
        public boolean shownByDefault() {
            return false;
        }
    }
}
