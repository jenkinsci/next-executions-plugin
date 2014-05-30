package hudson.plugins.nextexecutions;

import hudson.Extension;
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
}
