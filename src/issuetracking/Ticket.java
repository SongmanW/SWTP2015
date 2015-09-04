package issuetracking;

import java.io.Serializable;
import java.text.*;
import java.util.*;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name="tickets")
public class Ticket implements Serializable {
        public static final String BUG = "bug";
        public static final String FEATURE = "feature";
	
        @Id
	protected int id;
        @ManyToOne
        @JoinColumn(name = "sprintid")
	protected Sprint sprint;
	protected String title;
	protected String description;
        @Temporal(javax.persistence.TemporalType.DATE)
	protected Date creation_date;
	protected String author;
	protected String responsible_user;
	protected String type;
	protected String status;
        
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "tcrelation",
                joinColumns = {@JoinColumn(name = "tid")},
                inverseJoinColumns = {@JoinColumn(name = "compid")})
        private List<Component> components;
        
        @OneToMany(mappedBy="ticket", fetch = FetchType.EAGER)
        private List<Comment> comments;
    public List<Comment> getComments() {
        return comments;
    }
     
        private String estimated_time;

        public Ticket() {
            comments = new ArrayList<>();
            components = new ArrayList<>();
        }

	public Ticket(Sprint sprint, String title, String description,
			Date date, String author, String responsible_user, String type,
			String state) {
            this();
		this.sprint = sprint;
		this.title = title;
		this.description = description;
		this.creation_date = date;
		this.author = author;
		this.responsible_user = responsible_user;
		this.type = type;
		this.status = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
        
        public Sprint getSprint(){
            return sprint;
        }
        
        public void setSprint(Sprint sprint){
            this.sprint = sprint;
        }
	
	public int getSprintid() {
            if(sprint == null)
                return -1;
            return sprint.getSprintid();
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreation_date() {
		return creation_date;
	}

	public String getDateAsString() {
   		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
  		String date1=ft.format(creation_date);
		return date1;
	}
	
	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getResponsible_user() {
		return responsible_user;
	}

	public void setResponsible_user(String responsible_user) {
		this.responsible_user = responsible_user;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEstimated_time() {
		return estimated_time;
	}

	public void setEstimated_time(String estimated_time) {
            this.estimated_time = estimated_time;
	}
	
	
	public Map<String, String> validate(DBManager DBManager1) {
		Map<String, String> errorMsg = new HashMap<String, String>();
		if (title == null || title.trim().equals(""))
			errorMsg.put("title", "This field should not be empty!");
		if (description == null || description.trim().equals(""))
			errorMsg.put("description", "This field should not be empty!");
		if (author == null || author.trim().equals(""))
			errorMsg.put("author", "This field should not be empty!");
		if (!DBManager1.containsUser(author))
			errorMsg.put("author", "The user does not exist!");
		if (responsible_user == null || responsible_user.trim().equals(""))
			errorMsg.put("responsible_user",
					"This field should not be empty!");
		if (!DBManager1.containsUser(responsible_user))
			errorMsg.put("responsible_user", "The user does not exist!");
		if (type == null || type.trim().equals(""))
			errorMsg.put("type", "This field should not be empty!!");
		if (status == null || status.trim().equals(""))
			errorMsg.put("state", "This field should not be empty!!");
                
                if(FEATURE.equals(type)){
                    if (estimated_time == null || estimated_time.trim().equals(""))
			errorMsg.put("estimated_time",
					"estimated_time darf nicht leer sein!");
                }

		return errorMsg;
	}

    public void removeComponent(Component c) {
        components.remove(c);
    }
    
    public void addComponent(Component c){
        components.add(c);
    }
    
    public void clearComponents(){
        components.clear();
    }
    
    public List<Component> getComponents(){
        return components;
    }

}
