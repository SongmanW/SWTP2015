package action;

import issuetracking.DBManager;
import issuetracking.Ticket;
import issuetracking.User;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteUserAction implements Action {


	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
                DBManager DBManager1 = (DBManager) request.getAttribute("dao");
		User u1 = DBManager1.getUserByUserid(request.getParameter("user_id"));

		List<Ticket> tickets = DBManager1.getTickets();
		for (Ticket t1 : tickets) {
			if (t1.getAuthor().equals(u1.getUserid())) {
				DBManager1.deleteTicket(t1);
			} else if (t1.getResponsible_user().equals(u1.getUserid())) {
				t1.setResponsible_user(t1.getAuthor());
				DBManager1.updateTicket(t1);
			}
		}
		DBManager1.deleteUser(u1);

		if ("deleteUser_from_account".equals(request.getParameter("action"))){
			request.getSession().removeAttribute("user");
			request.getSession().removeAttribute("password");
			return "/user/login.jsp";}
		else
			return "/admin/users.jsp";
	}
}