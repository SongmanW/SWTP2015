package action;

import issuetracking.DBManager;
import issuetracking.User;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginAction implements Action {

	private static final DBManager DBManager1 = DBManager.getInstance();

	@Override
	public String execute(HttpServletRequest request,HttpServletResponse response) {

		Map<String, String> errorMsgs = new HashMap<String, String>();
		String useridinput = request.getParameter("useridinput");
		String passwordinput = request.getParameter("passwordinput");

		errorMsgs = User.validateUserLogin(useridinput, passwordinput);

		if (errorMsgs.isEmpty()) {
			if (DBManager1.checkLogin(useridinput, passwordinput)) {
				request.getSession().setAttribute("user", useridinput);
				request.getSession().setAttribute("password", passwordinput);

				return "index.jsp";
			} else {
				errorMsgs.put("useridinput", "Wrong username or password");
			}
		}
		request.setAttribute("errorMsgsLogin", errorMsgs);
		return "login.jsp";
	}

}