package hudson.plugins.nextexecutions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.kohsuke.stapler.Stapler;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.model.View;
import hudson.model.TopLevelItem;
import hudson.plugins.nextexecutions.NextBuilds.DescriptorImpl;
import hudson.plugins.nextexecutions.utils.NextExecutionsUtils;
import hudson.triggers.Trigger;
import hudson.triggers.TimerTrigger;
import hudson.widgets.Widget;

/**
 * Widget in the main sidebar with a list
 * of projects and their next scheduled 
 * build's date. The list is sorted by date.
 * 
 * @author ialbors
 *
 */
@Extension
public class NextExecutionsWidget extends Widget {
	private static final Logger LOGGER = Logger.getLogger(NextExecutionsWidget.class.getName());
	 
	protected Class<? extends Trigger> triggerClass;
	public NextExecutionsWidget() {
		triggerClass = TimerTrigger.class;
	}
	
	public List<NextBuilds> getBuilds() {
		List<NextBuilds> nblist = new Vector<NextBuilds>();

		List<AbstractProject> l;
		
		View v = Stapler.getCurrentRequest().findAncestorObject(View.class);
		
		DescriptorImpl d = (DescriptorImpl)(Hudson.getInstance().getDescriptorOrDie(NextBuilds.class));
		
		if(d.getFilterByView() && v != null)
		{
			Collection<TopLevelItem> tli = v.getItems();
			Vector<AbstractProject> vector = new Vector<AbstractProject>();
			for (TopLevelItem topLevelItem : tli) {
				if(topLevelItem instanceof AbstractProject){
					vector.add((AbstractProject)topLevelItem);
				}
			}
			l = vector;
		}
		else{
			l = Hudson.getInstance().getItems(AbstractProject.class); 
		}
		
		
		for (AbstractProject project: l) {
			NextBuilds nb = NextExecutionsUtils.getNextBuild(project, triggerClass);
			if(nb != null)
				nblist.add(nb);
		}
		Collections.sort(nblist);
		return nblist;
		
	}
	
	public String getWidgetName() {
		return Messages.NextExec_WidgetName();

	}
	
}
