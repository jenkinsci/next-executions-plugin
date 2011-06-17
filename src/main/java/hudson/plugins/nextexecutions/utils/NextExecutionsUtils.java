package hudson.plugins.nextexecutions.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import antlr.ANTLRException;

import hudson.model.AbstractProject;
import hudson.plugins.nextexecutions.NextBuilds;
import hudson.scheduler.CronTab;
import hudson.triggers.TimerTrigger;
import hudson.triggers.Trigger;

public class NextExecutionsUtils {

	/**
	 *   Returns the {@link NextBuild} for the project.
	 *
	 * @return The {@link NextBuild} object with the associated
	 * next execution date or null.
	 */
	public static NextBuilds getNextBuild(AbstractProject project){
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
					return new NextBuilds(project, cal);
			}
		}
		return null;
	}
	
	/**
	 * Pretty much the same as {@link CronTabList#create(String)}
	 */
	private static List<CronTab> parseSpec(String format) {
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
