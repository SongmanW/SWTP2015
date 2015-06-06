package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Action {
	String execute(HttpServletRequest request, HttpServletResponse response);

	public static Action actionFactory(String actionName) {
		switch (actionName) {
		case "addTicket":
			return new AddTicketAction();
		case "deleteTicket":
			return new DeleteTicketAction();
		case "changeTicket":
			return new ChangeTicketAction();
		case "login":
			return new LoginAction();
		case "logout":
			return new LogoutAction();
		case "register":
		case "register_from_users":
			return new RegisterAction();
		case "deleteUser":
		case "deleteUser_from_account":
			return new DeleteUserAction();
		case "changeUser":
		case "changeUser_from_account":
			return new ChangeUserAction();
		case "preparePage":
			return new PreparePageAction();
		default:
			throw new IllegalArgumentException("Invalid Action: " + actionName);
		}
	}
}