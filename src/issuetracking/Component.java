package issuetracking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "components")
public class Component {

    @Id
    protected Integer compid;
    protected String name;
    protected String description;

    @ManyToMany
    @JoinTable(name = "tcrelation",
            joinColumns = {
                @JoinColumn(name = "compid")},
            inverseJoinColumns = {
                @JoinColumn(name = "tid")})
    List<Ticket> tickets;

    public Component(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public Component() {
        tickets = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompid(Integer id) {
        this.compid = id;
    }

    public Integer getCompid() {
        return compid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> validate() {
        Map<String, String> errorMsg = new HashMap<String, String>();
        if (name == null || name.trim().equals("")) {
            errorMsg.put("id", "This field should not be empty!");
        }
        if (description == null || description.trim().equals("")) {
            errorMsg.put("description", "This field should not be empty!");
        }

        return errorMsg;
    }

    void addTicket(Ticket t) {
        tickets.add(t);
    }

    void removeTicket(Ticket t) {
        tickets.remove(t);
    }

    void clearTickets() {
        tickets.clear();
    }

    List<Ticket> getTickets() {
        return tickets;
    }
}
