package issuetracking;

import java.util.HashMap;
import java.util.Map;

public class TicketBug extends Ticket {

	public Map<String, String> validate() {
		Map<String, String> errorMsg = new HashMap<String, String>();
		if (title == null || title.trim().equals(""))// field is empty
			errorMsg.put("title", "Titel darf nicht leer sein!");
		if (description == null || description.trim().equals(""))// field is
			errorMsg.put("description", "Beschreibung darf nicht leer sein!");
		if (author == null || author.trim().equals(""))// field is empty
			errorMsg.put("author", "Author darf nicht leer sein!");
		if (!DBManager1.containsUser(author))
			errorMsg.put("author", "Der Autor existiert nicht!");
		if (responsible_user == null || responsible_user.trim().equals(""))
			errorMsg.put("responsible_user",
					"Responsible user darf nicht leer sein!");
		if (!DBManager1.containsUser(responsible_user))
			errorMsg.put("responsible_user", "Der verantwortliche Nutzer existiert nicht!");
		if (type == null || type.trim().equals(""))// field is empty
			errorMsg.put("type", "type darf nicht leer sein!");
		if (state == null || state.trim().equals(""))// field is empty
			errorMsg.put("state", "state darf nicht leer sein!");

		return errorMsg;
	}

}
