package hudson.plugins.nextexecutions;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import jenkins.model.Jenkins;

import org.kohsuke.stapler.Stapler;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.model.Queue;
import hudson.model.Queue.Item;
import hudson.model.Queue.WaitingItem;
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
 * It includes those in the Queue with an execution
 * delay of more than a minute.
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

		List<Job<?, ?>> l;
		
		View v = Stapler.getCurrentRequest().findAncestorObject(View.class);
		
		DescriptorImpl d = (DescriptorImpl)(Jenkins.getInstance().getDescriptorOrDie(NextBuilds.class));
		
		if(d.getFilterByView() && v != null)
		{
			Collection<TopLevelItem> tli = v.getItems();
			Vector<Job<?,?>> vector = new Vector<Job<?,?>>();
			for (TopLevelItem topLevelItem : tli) {
				if(topLevelItem instanceof Job<?,?>){
					vector.add((Job<?, ?>)topLevelItem);
				}
			}
			l = vector;
		}
		else{
		    l = new Vector<Job<?,?>>();
		    for (Job<?,?> j : Jenkins.getInstance().getItems(Job.class)) {
			l.add(j);
		    }
		}
		
		
		for (Job<?,?> project: l) {
			NextBuilds nb = NextExecutionsUtils.getNextBuild(project, triggerClass);
			if(nb != null)
				nblist.add(nb);
		}

		// Get also the items in the queue but only those that have a waiting
		// period of more than a minute.
		
		// Only for this class. Not its children
		if (this.getClass() == NextExecutionsWidget.class) {
			Item[] queueItems = Queue.getInstance().getItems();
			for (Item item : queueItems) {
				if(item instanceof WaitingItem && item.task instanceof AbstractProject) {
					WaitingItem waitingItem = (WaitingItem)item;
					Calendar now = Calendar.getInstance();
					long nowMilliseconds = now.getTimeInMillis();
					now.setTimeInMillis(nowMilliseconds + 60 * 1000);
					if(waitingItem.timestamp.after(now)) {
						NextBuilds nb = new NextBuilds((AbstractProject)item.task, waitingItem.timestamp);
						nblist.add(nb);
					}
			
				}
			}
		}

		Collections.sort(nblist);
		return nblist;
		
	}
	
	public String getWidgetName() {
		return Messages.NextExec_WidgetName();

	}
	
	public String getWidgetId() {
		return "next-exec";
	}
	
	public boolean showWidget() {
		return true;
	}

    public int getDisplayMode() {
        DescriptorImpl d =
          (DescriptorImpl)(Jenkins.getInstance().getDescriptorOrDie(NextBuilds.class));
        return d.getDisplayMode();
    }
	
}
