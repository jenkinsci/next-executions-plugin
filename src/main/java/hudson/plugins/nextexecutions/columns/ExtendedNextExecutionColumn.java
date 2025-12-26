package hudson.plugins.nextexecutions.columns;

import hudson.Extension;
import hudson.plugins.nextexecutions.NextBuilds;
import hudson.plugins.nextexecutions.utils.ExtendedNextExecutionsUtils;
import io.jenkins.plugins.extended_timer_trigger.ExtendedTimerTrigger;
import jenkins.model.ParameterizedJobMixIn;
import org.kohsuke.stapler.DataBoundConstructor;

public class ExtendedNextExecutionColumn extends NextExecutionColumnBase {

    @DataBoundConstructor
    public ExtendedNextExecutionColumn() {
        triggerClass = ExtendedTimerTrigger.class;
    }

    @Override
    public String getColumnId() {
        return "column-next-extended-launch";
    }

    @Override
    protected NextBuilds getNextBuild(ParameterizedJobMixIn.ParameterizedJob project) {
        return ExtendedNextExecutionsUtils.getNextBuild(project, triggerClass);
    }

    @Extension(optional = true)
    public static class DescriptorImpl extends NextExecutionColumnBase.DescriptorImpl {

        @Override
        public String getDisplayName() {
            return Messages.ExtendedExecutions_ColumnName();
        }
    }
}
