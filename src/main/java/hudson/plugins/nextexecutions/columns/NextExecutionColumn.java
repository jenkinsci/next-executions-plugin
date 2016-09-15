package hudson.plugins.nextexecutions.columns;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.model.Job;
import hudson.views.ListViewColumnDescriptor;
import hudson.views.ListViewColumn;
import hudson.plugins.nextexecutions.*;
import hudson.plugins.nextexecutions.utils.NextExecutionsUtils;
import hudson.triggers.TimerTrigger;
import hudson.triggers.Trigger;
import jenkins.model.ParameterizedJobMixIn;

/**
 * 
 * Column that shows the next scheduled date for a job.
 * 
 * @author ialbors
 *
 */

public class NextExecutionColumn extends ListViewColumn {

	protected Class<? extends Trigger> triggerClass;

	@DataBoundConstructor
	public NextExecutionColumn() {
	    triggerClass = TimerTrigger.class;
	}
	
	public String getNextExecution(Job job){
		if(job instanceof ParameterizedJobMixIn.ParameterizedJob){
			NextBuilds b = NextExecutionsUtils.getNextBuild((ParameterizedJobMixIn.ParameterizedJob)job, triggerClass);
			if(b != null)
				return b.getDate();
		}
		return "";
	}
	
	@Extension
	public static class DescriptorImpl extends ListViewColumnDescriptor {

		@Override
		public String getDisplayName() {
			// TODO Auto-generated method stub
			return Messages.NextExecutions_ColumnName();
		}
		@Override
		public boolean shownByDefault() {
			return false;
		}
		
	}

}
