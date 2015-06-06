package issuetracking;

import java.util.HashMap;
import java.util.Map;

public class TicketFeature extends Ticket { 

	private String estimated_time;

	public String getEstimated_time() {
		return estimated_time;
	}

	public void setEstimated_time(String estimated_time) {
		this.estimated_time = estimated_time;
	}

	public Map<String, String> validate() {
		Map<String, String> errorMsg = new HashMap<String, String>();
		if (title == null || title.trim().equals(""))
			errorMsg.put("title", "Titel darf nicht leer sein!");
		if (description == null || description.trim().equals(""))
			errorMsg.put("description", "Beschreibung darf nicht leer sein!");
		if (author == null || author.trim().equals(""))
			errorMsg.put("author", "Author darf nicht leer sein!");
		if (!DBManager1.containsUser(author))
			errorMsg.put("author", "Der Autor existiert nicht!");
		if (responsible_user == null || responsible_user.trim().equals(""))
			errorMsg.put("responsible_user",
					"Responsible user darf nicht leer sein!");
		if (!DBManager1.containsUser(responsible_user))
			errorMsg.put("responsible_user", "Der verantwortliche Nutzer existiert nicht!");
		if (type == null || type.trim().equals(""))
			errorMsg.put("type", "type darf nicht leer sein!");
		if (state == null || state.trim().equals(""))
			errorMsg.put("state", "state darf nicht leer sein!");
		if (estimated_time == null || estimated_time.trim().equals(""))
			errorMsg.put("estimated_time",
					"estimated_time darf nicht leer sein!");

		return errorMsg;
	}
}
