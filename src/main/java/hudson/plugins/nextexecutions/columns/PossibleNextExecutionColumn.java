package hudson.plugins.nextexecutions.columns;

import hudson.Extension;
import hudson.triggers.SCMTrigger;
import org.kohsuke.stapler.DataBoundConstructor;

public class PossibleNextExecutionColumn extends NextExecutionColumn {

    @DataBoundConstructor
    public PossibleNextExecutionColumn() {
        triggerClass = SCMTrigger.class;
    }

    @Override
    public String getColumnId() {
        return "column-next-possible-launch";
    }

    @Extension
    public static class DescriptorImpl extends NextExecutionColumn.DescriptorImpl {

        @Override
        public String getDisplayName() {
            return Messages.NextPossibleExecutions_ColumnName();
        }
    }
}
