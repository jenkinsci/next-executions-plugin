package hudson.plugins.nextexecutions;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import antlr.ANTLRException;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.scheduler.CronTab;
import hudson.triggers.TimerTrigger;
import hudson.triggers.Trigger;
import hudson.widgets.Widget;

@Extension
public class NextExecutionsWidget extends Widget {
	 private static final Logger LOGGER = Logger.getLogger(NextExecutionsWidget.class.getName());
	 
	public List<NextBuilds> getBuilds() {		
		List<NextBuilds> nblist = new Vector<NextBuilds>();
		
		List<AbstractProject> l = Hudson.getInstance().getItems(AbstractProject.class);
		
		for (AbstractProject project: l) {
			if(!project.isDisabled()){
				Trigger trigger = project.getTrigger(TimerTrigger.class);
				if(trigger != null){
					List<CronTab> crons = parseSpec(trigger.getSpec());
					Calendar cal = null;
					for (CronTab cronTab : crons) {
						Date d = new Date();				
						cal = (cal == null || cal.compareTo(cronTab.ceil(d.getTime())) > 0)? cronTab.ceil(d.getTime()) : cal;					
					}
					if(cal != null)
						nblist.add(new NextBuilds(project, cal));
				}
			}
		}
		Collections.sort(nblist);
		return nblist;
		
	}
	
	/**
	 * Pretty much the same as {@link CronTabList#create(String)}
	 */
	private List<CronTab> parseSpec(String format) {
		Vector<CronTab> r = new Vector<CronTab>();
        int lineNumber = 0;
        for (String line : format.split("\\r?\\n")) {
            lineNumber++;
            line = line.trim();
            if(line.length()==0 || line.startsWith("#"))
                continue;   // ignorable line
            try {
                r.add(new CronTab(line,lineNumber));
            } catch (ANTLRException e) {
                e.printStackTrace();
            }
        }
        return r;
	}
}
