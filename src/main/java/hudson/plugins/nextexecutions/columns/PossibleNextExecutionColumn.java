package hudson.plugins.nextexecutions.columns;

import hudson.Extension;
import hudson.plugins.nextexecutions.NextBuilds;
import hudson.plugins.nextexecutions.utils.NextExecutionsUtils;
import hudson.triggers.SCMTrigger;
import jenkins.model.ParameterizedJobMixIn;
import org.kohsuke.stapler.DataBoundConstructor;

public class PossibleNextExecutionColumn extends NextExecutionColumnBase {

    @DataBoundConstructor
    public PossibleNextExecutionColumn() {
        triggerClass = SCMTrigger.class;
    }

    @Override
    public String getColumnId() {
        return "column-next-possible-launch";
    }

    @Override
    protected NextBuilds getNextBuild(ParameterizedJobMixIn.ParameterizedJob project) {
        return NextExecutionsUtils.getNextBuild(project, triggerClass);
    }

    @Extension
    public static class DescriptorImpl extends NextExecutionColumnBase.DescriptorImpl {

        @Override
        public String getDisplayName() {
            return Messages.NextPossibleExecutions_ColumnName();
        }
    }
}
