package hudson.plugins.nextexecutions;

import hudson.Extension;
import hudson.model.Describable;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.plugins.nextexecutions.Messages;
import hudson.util.FormValidation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

public class NextBuilds implements Comparable, Describable<NextBuilds>{
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
		String dateFormat = this.getDescriptor().getDateFormat();
		if(dateFormat == null){
			dateFormat = this.getDescriptor().getDefault();
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
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

	public DescriptorImpl getDescriptor() {
		 return (DescriptorImpl)Hudson.getInstance().getDescriptorOrDie(getClass());		
	}
		
	@Extension
	public static class DescriptorImpl extends Descriptor<NextBuilds>  {
		private String dateFormat;
		
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

		
		public String getDefault() {
			return "dd/MM/yyyy HH:mm";
		}

				
		@Override
		public boolean configure(StaplerRequest req, JSONObject json)
				throws hudson.model.Descriptor.FormException {
			dateFormat = json.getString("dateFormat");
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
