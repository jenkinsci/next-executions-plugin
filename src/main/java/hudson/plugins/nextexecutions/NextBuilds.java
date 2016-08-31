package hudson.plugins.nextexecutions;

import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.plugins.nextexecutions.Messages;
import hudson.util.FormValidation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jenkins.model.Jenkins;
import net.sf.json.JSONObject;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import hudson.Util;

/**
 * Provides a way to get the project's next execution date.
 * 
 */
public class NextBuilds implements Comparable, Describable<NextBuilds>{
	private Job<?, ?> project;
	private String name;
	private String dateString;
	private Calendar date;
	
	public NextBuilds(Job<?,?> project, Calendar date) {
		this.project = project;
		this.name = Util.escape(project.getDisplayName());
		this.date = date;
	}
	
	private String formatDate(Date d) {
		String dateFormat = this.getDescriptor().getDateFormat();
		if(dateFormat == null){
			dateFormat = this.getDescriptor().getDefault();
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(d.getTime());
	}
	
	public String getDate() {
		return formatDate(date.getTime());
	}
	
	public String getTimeToGo() {
		DateTime now = new DateTime();
		
		PeriodType periodType = PeriodType.dayTime();
		periodType.withMillisRemoved();
		Period timeToGo = new Period(now, new DateTime(date.getTimeInMillis()),
				periodType);  

		PeriodFormatter pf = new PeriodFormatterBuilder().
			appendDays().
			appendSuffix("d").
			appendSeparatorIfFieldsBefore(" ").
			appendHours().
			appendSuffix("h").
			appendSeparatorIfFieldsBefore(" ").
			appendMinutes().
			appendSuffix("m").
			toFormatter();
		
		return Messages.TimeToGo(pf.print(timeToGo));
	}
	
	public String getName() {
		return name;
	}
	
	public String getUrl(){
		return Jenkins.getInstance().getRootUrl() + project.getUrl();
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

	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl)Jenkins.getInstance().getDescriptorOrDie(getClass());
	}
	
	@Extension
	public static class DescriptorImpl extends Descriptor<NextBuilds>  {
		private String dateFormat;
		private Boolean filterByView;
		private Boolean showPossibleWidget;
		private Integer displayMode;
		
		public DescriptorImpl() {
			load();
		}
		
		@Override
		public String getDisplayName() {
			return "TESTING ";
		}
		
		public String getDateFormat() {
			return dateFormat;
		}

		public boolean getFilterByView() {
			if(filterByView == null)
				return getFilterByViewDefault();
			return filterByView;
		}
		
		public boolean getShowPossibleWidget() {
			if(showPossibleWidget == null)
				return false;
			return showPossibleWidget;
		}
		
		
		public Integer getDisplayMode() {
			if(displayMode == null) {
				return 1;
			}
			return displayMode;
		}

		public String getDefault() {
			return "dd/MM/yyyy HH:mm";
		}
		
		public boolean getFilterByViewDefault() {
			return true;
		}
		
		@Override
		public boolean configure(StaplerRequest req, JSONObject json)
				throws hudson.model.Descriptor.FormException {
			dateFormat = json.getString("dateFormat");
			filterByView = json.getBoolean("filterByView");
			showPossibleWidget = json.getBoolean("showPossibleWidget");
			displayMode = json.getInt("displayMode");
			save();
			return true;
		}
		
		public FormValidation doCheckDateFormat(@QueryParameter String value) {
			try{
				SimpleDateFormat sdf = new SimpleDateFormat(value);
				return FormValidation.ok();
			}
			catch (IllegalArgumentException e) {
				return FormValidation.error(Messages.Format_Error());
			}
		}
	}
}
