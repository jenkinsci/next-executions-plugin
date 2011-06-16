package hudson.plugins.nextexecutions.columns;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.views.ListViewColumnDescriptor;
import hudson.views.ListViewColumn;

public class NextExecutionColumn extends ListViewColumn {

	@DataBoundConstructor
	public NextExecutionColumn() {
	}
	
	@Extension
	public static class DescriptorImpl extends ListViewColumnDescriptor {

		@Override
		public String getDisplayName() {
			// TODO Auto-generated method stub
			return "TEST";
		}
		@Override
		public boolean shownByDefault() {
			return false;
		}
		
	}

}
