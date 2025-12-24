package hudson.plugins.nextexecutions.columns;

import hudson.Extension;
import org.jenkinsci.plugins.parameterizedscheduler.ParameterizedTimerTrigger;
import org.kohsuke.stapler.DataBoundConstructor;

public class ParameterizedNextExecutionColumn extends NextExecutionColumn {

    @DataBoundConstructor
    public ParameterizedNextExecutionColumn() {
        triggerClass = ParameterizedTimerTrigger.class;
    }

    @Override
    public String getColumnId() {
        return "column-next-parameterized-launch";
    }

    @Extension(optional = true)
    public static class DescriptorImpl extends NextExecutionColumn.DescriptorImpl {

        @Override
        public String getDisplayName() {
            return Messages.ParameterizedExecutions_ColumnName();
        }
    }
}
