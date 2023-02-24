package hudson.plugins.nextexecutions;

import jenkins.model.Jenkins;
import hudson.Extension;
import hudson.plugins.nextexecutions.NextBuilds.DescriptorImpl;
import hudson.triggers.SCMTrigger;

@Extension
public class PossibleNextExecutionsWidget extends NextExecutionsWidget {

	public PossibleNextExecutionsWidget() {
		triggerClass = SCMTrigger.class;
	}
	
	@Override
	public String getWidgetName() {
		return Messages.PossibleNextExec_WidgetName();
	}
	
	@Override
	public boolean showWidget() {
		Jenkins j = Jenkins.getInstanceOrNull();
		DescriptorImpl d = j != null ? (DescriptorImpl)(j.getDescriptorOrDie(NextBuilds.class)) : null;

		if (d == null){
			return false;
		}
		return d.getShowPossibleWidget();
	}
	
	@Override
	public String getWidgetId() {
		return super.getWidgetId() + "-possible";
	}
}
