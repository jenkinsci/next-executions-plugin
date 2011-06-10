package hudson.plugins.nextexecutions;

import hudson.model.AbstractProject;
import hudson.model.Hudson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NextBuilds implements Comparable{
	private AbstractProject project;
	private String name;
	private String dateString;
	private Calendar date;
	
	public NextBuilds(AbstractProject project, Calendar date) {
		this.project = project;
		this.name = project.getDisplayName();
		this.date = date;
	}
	
	public String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return sdf.format(date.getTime());
	}
	
	public String getName() {
		return name;
	}
	
	public String getUrl(){
		return Hudson.getInstance().getRootUrl() + project.getUrl();
	}
	
	public String getshortName() {		
		return (name.length() > 22)? name.substring(0, 19) + "...": name;		
	}

	public int compareTo(Object o) {
		if(o instanceof NextBuilds){
			NextBuilds toCompare = (NextBuilds)o;
			return this.date.compareTo(toCompare.date);
			
		}
		else{
			return 0;
		}
	}
		
}
