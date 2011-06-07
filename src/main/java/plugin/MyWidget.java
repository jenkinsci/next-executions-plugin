package plugin;

import java.util.List;
import java.util.logging.Logger;

import hudson.Extension;
import hudson.model.TopLevelItem;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.model.Job;
import hudson.widgets.Widget;

@Extension
public class MyWidget extends Widget {
	 private static final Logger LOGGER = Logger.getLogger(MyWidget.class.getName());
	 
	public String getBuilds() {
		List<AbstractProject> l = Hudson.getInstance().getItems(AbstractProject.class);
		for (AbstractProject topLevelItem : l) {
			topLevelItem.getTriggers();
			LOGGER.info("aaa" + topLevelItem.getName());
		}
//		List<Job> jobs = Hudson.getInstance().getAllItems(Job.class);
//		String string = "aa";
//		for (Job job : jobs) {
//			string += "\n" + job.getName() + " " + job.getClass();
//			System.out.println(job.getName());
//		}
		LOGGER.info("aaa");
		return "aa";
		
	}
}
