package hudson.plugins.nextexecutions.utils;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import hudson.model.AbstractProject;
import hudson.plugins.nextexecutions.NextBuilds;
import hudson.scheduler.CronTab;
import hudson.scheduler.CronTabList;
import hudson.triggers.TimerTrigger;
import hudson.triggers.Trigger;

public class NextExecutionsUtils {

	/**
	 * Returns the {@link NextBuild} for the project.
	 *
	 * @return The {@link NextBuild} object with the associated
	 * next execution date or null.
	 */
	public static NextBuilds getNextBuild(AbstractProject project){
		if(!project.isDisabled()){
			Trigger trigger = project.getTrigger(TimerTrigger.class);
			if(trigger != null){
				try{
				Field triggerTabsField = Trigger.class.getDeclaredField("tabs");
				triggerTabsField.setAccessible(true);
				
				CronTabList cronTabList = (CronTabList)triggerTabsField.get(trigger);
				
				Field crontablistTabsField = CronTabList.class.getDeclaredField("tabs");
				crontablistTabsField.setAccessible(true);
				
				List<CronTab> crons = (Vector<CronTab>)crontablistTabsField.get(cronTabList);
				
				Calendar cal = null;
				for (CronTab cronTab : crons) {
					Date d = new Date();
					cal = (cal == null || cal.compareTo(cronTab.ceil(d.getTime())) > 0)? cronTab.ceil(d.getTime()) : cal;
				}
				if(cal != null)
					return new NextBuilds(project, cal);
				}
				catch(NoSuchFieldException e){
					e.printStackTrace();
				}
				catch(IllegalAccessException e){
					e.printStackTrace();
				}
				
			}
		}
		return null;
	}
}
