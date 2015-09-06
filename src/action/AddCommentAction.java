package action;

import issuetracking.Comment;
import issuetracking.DBManager;
import issuetracking.Ticket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddCommentAction implements Action {

    @Override
    public String execute(HttpServletRequest request,
            HttpServletResponse response) {

        DBManager DBManager1 = (DBManager) request.getAttribute("dao");
        Map<String, String> errorMsgs = new HashMap<String, String>();

        DateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        Date date = null;
        try {
            date = format.parse(request.getParameter("date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Ticket ticket = DBManager1.getTicketById(Integer.parseInt(request.getParameter("ticket_id")));
        String author = request.getUserPrincipal().getName();
        String message = request.getParameter("message");
        Comment comment1 = new Comment(ticket, date, author, message);
        ticket.getComments().add(comment1);

        errorMsgs = comment1.validate();

        if (errorMsgs.isEmpty()) {
            DBManager1.saveComment(comment1);
        }

        request.setAttribute("errorMsgs", errorMsgs);
        return "/user/ticketview.jsp";
    }

}
