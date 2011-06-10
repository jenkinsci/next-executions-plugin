package hudson.plugins.nextexecutions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NextBuilds implements Comparable{
	private String name;
	private String dateString;
	private Calendar date;
	
	public NextBuilds(String name, Calendar date) {
		this.name = name;
		this.date = date;
	}
	
	public String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return sdf.format(date.getTime());
	}
	
	public String getName() {
		return name;
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
