package issuetracking;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Sprint {
	int sprintid;
	String title;
	protected Date start_date;
	protected Date end_date;
	boolean active;
	
public Sprint(int sprintid, String title, Date start_date, Date end_date,
			boolean active) {
		this.sprintid = sprintid;
		this.title = title;
		this.start_date = start_date;
		this.end_date = end_date;
		this.active = active;
	}


public int getSprintid() {
	return sprintid;
}
public void setSprintid(int sprintid) {
	this.sprintid = sprintid;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public Date getStart_date() {
	return start_date;
}
public void setStart_date(Date start_date) {
	this.start_date = start_date;
}
public Date getEnd_date() {
	return end_date;
}
public void setEnd_date(Date end_date) {
	this.end_date = end_date;
}

public String getStartDateAsString() {
		SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy HH:mm:ss");
		String date1=ft.format(start_date);
	return date1;
}
public String getStartDateAsStringForDatabase() {
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		String date1=ft.format(start_date);
	return date1;
}

public String getEndDateAsString() {
	SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy HH:mm:ss");
	String date1=ft.format(end_date);
return date1;
}
public String getEndDateAsStringForDatabase() {
	SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	String date1=ft.format(end_date);
return date1;
}

public boolean isActive() {
	return active;
}
public void setActive(boolean active) {
	this.active = active;
}



public Map<String, String> validate() {
	Map<String, String> errorMsg = new HashMap<String, String>();
	if (title == null || title.trim().equals(""))
		errorMsg.put("message", "This field should not be empty!");
	if (start_date == null)
		errorMsg.put("message", "This field should not be empty!");
	if (end_date == null)
		errorMsg.put("message", "This field should not be empty!");
	return errorMsg;
}

public String toString(){
	return ("" + this.sprintid + ", " + this.title + ", " + this.start_date.toString() + ", " + this.end_date.toString() + ", " + this.active);
}
}
