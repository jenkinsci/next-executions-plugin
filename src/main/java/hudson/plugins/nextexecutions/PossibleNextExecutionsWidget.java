package hudson.plugins.nextexecutions;

import hudson.Extension;

@Extension
public class PossibleNextExecutionsWidget extends NextExecutionsWidget {

	@Override
	public String getWidgetName() {
		return Messages.PossibleNextExec_WidgetName();
	}
}
