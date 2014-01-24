package hudson.plugins.nextexecutions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.kohsuke.stapler.Stapler;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.ComputerPanelBox;
import hudson.model.Hudson;
import hudson.model.TopLevelItem;
import hudson.model.View;
import hudson.plugins.nextexecutions.NextBuilds.DescriptorImpl;
import hudson.plugins.nextexecutions.utils.NextExecutionsUtils;


/**
 * Widget in the {@link Computer} page's sidebar with a list
 * of projects (tied to that Computer) and their next scheduled 
 * build's date. The list is sorted by date.
 * 
 * @author ialbors
 *
 */
@Extension
public class NextExecutionsComputerWidget extends ComputerPanelBox {
	
	public List<NextBuilds> getBuilds() {		
		List<NextBuilds> nblist = new Vector<NextBuilds>();

		List<AbstractProject> l = getComputer().getTiedJobs();				
		
		for (AbstractProject project: l) {
			NextBuilds nb = NextExecutionsUtils.getNextBuild(project);
			if(nb != null)
				nblist.add(nb);
		}
		Collections.sort(nblist);
		return nblist;
		
	}
}
