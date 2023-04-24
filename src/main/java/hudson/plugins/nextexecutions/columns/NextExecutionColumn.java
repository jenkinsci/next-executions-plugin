package hudson.plugins.nextexecutions.columns;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.model.Job;
import hudson.views.ListViewColumnDescriptor;
import hudson.views.ListViewColumn;
import hudson.plugins.nextexecutions.*;
import hudson.plugins.nextexecutions.utils.NextExecutionsUtils;
import hudson.plugins.nextexecutions.utils.ParameterizedNextExecutionsUtils;
import hudson.triggers.TimerTrigger;
import hudson.triggers.Trigger;
import jenkins.model.Jenkins;
import jenkins.model.ParameterizedJobMixIn;

@SuppressWarnings("rawtypes")
public class NextExecutionColumn extends ListViewColumn {

	protected Class<? extends Trigger> triggerClass;

	@DataBoundConstructor
	public NextExecutionColumn() {
	    triggerClass = TimerTrigger.class;
	}
	
	public String getNextExecution(Job job) {
		if(job instanceof ParameterizedJobMixIn.ParameterizedJob){
			NextBuilds b = NextExecutionsUtils.getNextBuild((ParameterizedJobMixIn.ParameterizedJob)job, triggerClass);
			if(b != null) {
				return b.getDate();
			}
			// Check parameterized 
			else if (getShowParameterizedWidget()) {
				b = ParameterizedNextExecutionsUtils.getNextBuild((ParameterizedJobMixIn.ParameterizedJob)job, triggerClass);
				if(b != null) {
					return b.getDate();
				}
			}
		}
		return "";
	}

	public boolean getShowParameterizedWidget() {
		Jenkins j = Jenkins.getInstanceOrNull();
        NextBuilds.DescriptorImpl d = j != null ? (NextBuilds.DescriptorImpl)(j.getDescriptorOrDie(NextBuilds.class)) : null;
		if (d == null) {
			return false;
		}
        return d.getShowParameterizedWidget();
	}
	
	@Extension
	public static class DescriptorImpl extends ListViewColumnDescriptor {

		@Override
		public String getDisplayName() {
			return Messages.NextExecutions_ColumnName();
		}
		@Override
		public boolean shownByDefault() {
			return false;
		}
		
	}

}
