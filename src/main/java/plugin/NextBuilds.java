package plugin;

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
		return SimpleDateFormat.getInstance().format(date.getTime());
	}
	
	public String getName() {
		return name;
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
