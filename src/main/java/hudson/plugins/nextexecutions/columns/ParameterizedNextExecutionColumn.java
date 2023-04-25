package hudson.plugins.nextexecutions.columns;

import org.jenkinsci.plugins.parameterizedscheduler.ParameterizedTimerTrigger;
import org.kohsuke.stapler.DataBoundConstructor;
import hudson.Extension;

public class ParameterizedNextExecutionColumn extends NextExecutionColumn {

	@DataBoundConstructor
	public ParameterizedNextExecutionColumn() {
	    triggerClass = ParameterizedTimerTrigger.class;
	}

	@Extension(optional = true)
	public static class DescriptorImpl extends NextExecutionColumn.DescriptorImpl {

		@Override
		public String getDisplayName() {
			return Messages.ParameterizedExecutions_ColumnName();
		}
	}
}
