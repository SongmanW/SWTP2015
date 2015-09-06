package issuetracking;

import java.io.Serializable;
import java.text.*;
import java.util.*;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name="comments")
public class Comment implements Serializable {
        @Id
	protected int cid;
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="tid")
	protected Ticket ticket;
	protected String author;
        @Temporal(javax.persistence.TemporalType.DATE)
	protected Date creation_date;
	protected String message;

    public Comment() {
    }
	
	public Comment(Ticket ticket, Date creation_date,String author, String message) {
		this.ticket=ticket;
		this.creation_date=creation_date;
		this.author=author;
		this.message=message;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}
    
	public Ticket getTicket() {
		return ticket;
	}
        
        public void setTicket(Ticket ticket){
            this.ticket = ticket;
        }

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getCreation_date() {
		return creation_date;
	}

	public String getDateAsString() {
   		SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy HH:mm:ss");
  		String date1=ft.format(creation_date);
		return date1;
	}
	public String getDateAsStringForDatabase() {
   		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
  		String date1=ft.format(creation_date);
		return date1;
	}
	
	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public Map<String, String> validate() {
		Map<String, String> errorMsg = new HashMap<String, String>();
		if (message == null || message.trim().equals(""))
			errorMsg.put("message", "This field should not be empty!");
		return errorMsg;
	}

	public String toString(){
		return ("" + this.cid + ", " + this.ticket + ", " + this.creation_date.toString() + ", " + this.author +", " + this.message );
	}
	
	
}

