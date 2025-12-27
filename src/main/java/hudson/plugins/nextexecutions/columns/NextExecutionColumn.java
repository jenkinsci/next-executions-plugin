package hudson.plugins.nextexecutions.columns;

import hudson.Extension;
import hudson.plugins.nextexecutions.NextBuilds;
import hudson.plugins.nextexecutions.utils.NextExecutionsUtils;
import hudson.triggers.TimerTrigger;
import jenkins.model.ParameterizedJobMixIn;
import org.kohsuke.stapler.DataBoundConstructor;

public class NextExecutionColumn extends NextExecutionColumnBase {

    @DataBoundConstructor
    public NextExecutionColumn() {
        triggerClass = TimerTrigger.class;
    }

    @Override
    public String getColumnId() {
        return "column-next-launch";
    }

    @Override
    protected NextBuilds getNextBuild(ParameterizedJobMixIn.ParameterizedJob project) {
        return NextExecutionsUtils.getNextBuild(project, triggerClass);
    }

    @Extension
    public static class DescriptorImpl extends NextExecutionColumnBase.DescriptorImpl {

        @Override
        public String getDisplayName() {
            return Messages.NextExecutions_ColumnName();
        }
    }
}
