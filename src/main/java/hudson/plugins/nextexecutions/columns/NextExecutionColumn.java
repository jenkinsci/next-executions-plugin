package hudson.plugins.nextexecutions.columns;

import java.util.Calendar;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.model.Run;
import hudson.views.ListViewColumnDescriptor;
import hudson.views.ListViewColumn;
import hudson.plugins.nextexecutions.*;
import hudson.plugins.nextexecutions.utils.NextExecutionsUtils;

public class NextExecutionColumn extends ListViewColumn {

	@DataBoundConstructor
	public NextExecutionColumn() {
	}
	
	public String getNextExecution(Job job){
		if(job instanceof AbstractProject){
			NextBuilds b = NextExecutionsUtils.getNextBuild((AbstractProject)job);
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
