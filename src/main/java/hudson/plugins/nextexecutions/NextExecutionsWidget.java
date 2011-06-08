package hudson.plugins.nextexecutions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import antlr.ANTLRException;

import hudson.Extension;
import hudson.model.TopLevelItem;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.model.Job;
import hudson.scheduler.CronTab;
import hudson.triggers.TimerTrigger;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import hudson.widgets.Widget;

@Extension
public class NextExecutionsWidget extends Widget {
	 private static final Logger LOGGER = Logger.getLogger(NextExecutionsWidget.class.getName());
	 
	public List<NextBuilds> getBuilds() {		
		List<NextBuilds> nblist = new Vector<NextBuilds>();
		
		List<AbstractProject> l = Hudson.getInstance().getItems(AbstractProject.class);
		
		for (AbstractProject project: l) {
			Trigger triggers = project.getTrigger(TimerTrigger.class);
			LOGGER.info(triggers.getSpec());
			try {
				CronTab c = new CronTab(triggers.getSpec());
				Date d = new Date();				
				Calendar cal = c.ceil(d.getTime());
				nblist.add(new NextBuilds(project.getName(), cal));			
				LOGGER.info("" + SimpleDateFormat.getInstance().format(cal.getTime()));
			} catch (ANTLRException e) {

				e.printStackTrace();
			}
			LOGGER.info("aaa" + project.getName());
		}
		Collections.sort(nblist);
		return nblist;
		
	}
}
