package action;

import issuetracking.DBManager;
import issuetracking.User;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterAction implements Action {


	@Override
	public String execute(HttpServletRequest request,HttpServletResponse response) {
                DBManager DBManager1 = (DBManager) request.getAttribute("dao");

		Map<String, String> errorMsgs = new HashMap<String, String>();
		String regSuccess = null;
		String useridinput = request.getParameter("useridinput");
		String passwordinput = request.getParameter("passwordinput");

		errorMsgs = User.validateUserRegistration(useridinput, passwordinput, DBManager1);
		if (errorMsgs.isEmpty()) {
			DBManager1.registerUser(useridinput, passwordinput);
			regSuccess = "Du wurdest registriert";
		}
		request.setAttribute("errorMsgsReg", errorMsgs);
		request.setAttribute("regSuccess", regSuccess);
		if ("register".equals(request.getParameter("action")))
			return "/login.jsp";
		else
			return "/admin/users.jsp";
	}
}