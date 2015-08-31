package issuetracking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "components")
public class Component {
        @Id
	protected String compid;
	protected String description;
        
        @ManyToMany
        @JoinTable(name = "tcrelation")
        List<Ticket> tickets;
        
	public Component(String compid, String description) {
            this();
		this.compid = compid;
		this.description = description;
	}
	
	public Component() {
            tickets = new ArrayList<>();
	}

	public String getCompid() {
		return compid;
	}

	public void setCompid(String compid) {
		this.compid = compid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Map<String, String> validate() {
		Map<String, String> errorMsg = new HashMap<String, String>();
		if (compid == null || compid.trim().equals(""))
			errorMsg.put("id", "This field should not be empty!");
		if (description == null || description.trim().equals(""))
			errorMsg.put("description", "This field should not be empty!");

		return errorMsg;
	}

        void addTicket(Ticket t){
            tickets.add(t);
        }
        
        void removeTicket(Ticket t){
            tickets.remove(t);
        }
        
        void clearTickets() {
            tickets.clear();
        }
        
        List<Ticket> getTickets(){
            return tickets;
        }
}
