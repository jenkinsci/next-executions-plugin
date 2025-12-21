package hudson.plugins.nextexecutions.columns;

import hudson.Extension;
import hudson.plugins.nextexecutions.NextBuilds;
import hudson.plugins.nextexecutions.utils.ExtendedNextExecutionsUtils;
import io.jenkins.plugins.extended_timer_trigger.ExtendedTimerTrigger;
import jenkins.model.ParameterizedJobMixIn;
import org.kohsuke.stapler.DataBoundConstructor;

public class ExtendedNextExecutionColumn extends NextExecutionColumn {

    @DataBoundConstructor
    public ExtendedNextExecutionColumn() {
        triggerClass = ExtendedTimerTrigger.class;
    }

    protected NextBuilds getNextBuild(ParameterizedJobMixIn.ParameterizedJob project) {
        return ExtendedNextExecutionsUtils.getNextBuild(project, triggerClass);
    }

    @Extension(optional = true)
    public static class DescriptorImpl extends NextExecutionColumn.DescriptorImpl {

        @Override
        public String getDisplayName() {
            return Messages.ExtendedExecutions_ColumnName();
        }
    }
}
