package hudson.plugins.nextexecutions.columns;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.model.Job;
import hudson.views.ListViewColumnDescriptor;
import hudson.views.ListViewColumn;
import hudson.plugins.nextexecutions.*;
import hudson.plugins.nextexecutions.utils.NextExecutionsUtils;
import hudson.triggers.SCMTrigger;
import jenkins.model.ParameterizedJobMixIn;

/**
 *
 * Column that shows the next possible scheduled date for a job.
 *
 * @author ialbors
 *
 */

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
