package issuetracking;

import java.util.HashMap;
import java.util.Map;

public class Ticket {

	private int id;
	private String title;
	private String description;

	public Ticket() {

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	
	public Map<String, String> validate() {
		Map<String, String> errorMsg = new HashMap<String, String>();
		if (title == null || title.trim().equals(""))// field is empty
			errorMsg.put("title", "Titel darf nicht leer sein!");
		if (description == null || description.trim().equals(""))// field is empty
			errorMsg.put("description", "Beschreibung darf nicht leer sein!");
		return errorMsg;
	}
	
}
