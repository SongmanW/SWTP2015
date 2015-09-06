package action;

import issuetracking.DBManager;
import issuetracking.Ticket;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteTicketAction implements Action {

    @Override
    public String execute(HttpServletRequest request,
            HttpServletResponse response) {
        DBManager DBManager1 = (DBManager) request.getAttribute("dao");

        Ticket t1 = DBManager1.getTicketById(Integer.parseInt(request.getParameter("ticket_id")));
        DBManager1.deleteTicket(t1);
        return "/user/index.jsp";
    }
}
