package issuetracking;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "sprints")
public class Sprint implements Serializable {
        @Id
	int sprintid;
	String title;
        @Temporal(javax.persistence.TemporalType.DATE)
	protected Date start_date;
        @Temporal(javax.persistence.TemporalType.DATE)
	protected Date end_date;
	boolean active;
        
        @OneToMany(fetch = FetchType.EAGER)
        @JoinColumn(name="sprintid")
        List<Ticket> tickets;

    public Sprint() {
        tickets = new ArrayList<>();
    }
	
public Sprint(String title, Date start_date, Date end_date,
			boolean active) {
                this();
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

public boolean getActive(){
    return active;
}

public void setActive(boolean active) {
	this.active = active;
}

public void addTicket(Ticket t){
    tickets.add(t);
}

public void removeTicket(Ticket t){
    tickets.remove(t);
}

public void clearTickets(){
    tickets.clear();
}

public List<Ticket> getTickets(){
    return tickets;
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

@Override
public String toString(){
	return ("" + this.sprintid + ", " + this.title + ", " + this.start_date.toString() + ", " + this.end_date.toString() + ", " + this.active);
}
}
