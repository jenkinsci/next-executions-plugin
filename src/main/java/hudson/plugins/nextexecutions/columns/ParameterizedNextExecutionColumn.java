package hudson.plugins.nextexecutions.columns;

import hudson.Extension;
import hudson.plugins.nextexecutions.NextBuilds;
import hudson.plugins.nextexecutions.utils.ParameterizedNextExecutionsUtils;
import jenkins.model.ParameterizedJobMixIn;
import org.jenkinsci.plugins.parameterizedscheduler.ParameterizedTimerTrigger;
import org.kohsuke.stapler.DataBoundConstructor;

public class ParameterizedNextExecutionColumn extends NextExecutionColumnBase {

    @DataBoundConstructor
    public ParameterizedNextExecutionColumn() {
        triggerClass = ParameterizedTimerTrigger.class;
    }

    @Override
    public String getColumnId() {
        return "column-next-parameterized-launch";
    }

    @Override
    protected NextBuilds getNextBuild(ParameterizedJobMixIn.ParameterizedJob project) {
        return ParameterizedNextExecutionsUtils.getNextBuild(project, triggerClass);
    }

    @Extension(optional = true)
    public static class DescriptorImpl extends NextExecutionColumnBase.DescriptorImpl {

        @Override
        public String getDisplayName() {
            return Messages.ParameterizedExecutions_ColumnName();
        }
    }
}
