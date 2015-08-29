package action;
import javax.servlet.http.*;
import issuetracking.DBManager;
import issuetracking.Ticket;

;

public class DeleteTicketAction implements Action {
	private static final DBManager DBManager1 = DBManager.getInstance();

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {

		Ticket t1 = DBManager1.getTicketById(Integer.parseInt(request.getParameter("ticket_id")));
		DBManager1.deleteTicket(t1);
		DBManager1.removeTCRelation(t1);
		return "user/index.jsp";
	}
}