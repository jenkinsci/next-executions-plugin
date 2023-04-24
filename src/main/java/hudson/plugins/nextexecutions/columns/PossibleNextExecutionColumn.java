package hudson.plugins.nextexecutions.columns;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.triggers.SCMTrigger;

public class PossibleNextExecutionColumn extends NextExecutionColumn {

	@DataBoundConstructor
	public PossibleNextExecutionColumn() {
	    triggerClass = SCMTrigger.class;
	}

	@Extension
	public static class DescriptorImpl extends NextExecutionColumn.DescriptorImpl {

		@Override
		public String getDisplayName() {
			return Messages.NextPossibleExecutions_ColumnName();
		}
	}
}
