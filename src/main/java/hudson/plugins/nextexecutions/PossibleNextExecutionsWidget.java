package hudson.plugins.nextexecutions;

import hudson.Extension;
import hudson.model.Hudson;
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
		DescriptorImpl d = (DescriptorImpl)(Hudson.getInstance().getDescriptorOrDie(NextBuilds.class));
		return d.getShowPossibleWidget();
	}
	
	@Override
	public String getWidgetId() {
		return super.getWidgetId() + "-possible";
	}
}
