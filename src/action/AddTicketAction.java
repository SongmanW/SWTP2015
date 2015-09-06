package action;

import issuetracking.Component;
import issuetracking.DBManager;
import issuetracking.Sprint;
import issuetracking.Ticket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddTicketAction implements Action {

    @Override
    public String execute(HttpServletRequest request,
            HttpServletResponse response) {
        DBManager DBManager1 = (DBManager) request.getAttribute("dao");
        Component tempcomp;
        Map<String, String> errorMsgs = new HashMap<String, String>();
        String type = request.getParameter("type");
        if (Ticket.BUG.equals(type) || Ticket.FEATURE.equals(type)) {
            String dateString = request.getParameter("date");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            try {
                date = format.parse(dateString);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            Sprint sprint = DBManager1.getSprintById(Integer.parseInt(request.getParameter("sprintid")));
            Ticket ticket = new Ticket(sprint, request.getParameter("title"), request.getParameter("description"), date, request.getUserPrincipal().getName(), request.getParameter("responsible_user"), request.getParameter("type"), request.getParameter("state")
            );
            ticket.setEstimated_time(request.getParameter("estimated_time"));

            String[] compids = request.getParameterValues("compid");
            if (compids != null) {
                for (String sCompid : compids) {
                    Integer iCompid = Integer.parseInt(sCompid);
                    Component comp1 = DBManager1.getComponentById(iCompid);
                    ticket.addComponent(comp1);
                }
            }

            errorMsgs = ticket.validate(DBManager1);

            if (errorMsgs.isEmpty()) {
                ticket.setId(DBManager1.saveTicket(ticket));

            }
        } else {
            errorMsgs.put("type", "Type not available");
        }

        request.setAttribute("errorMsgs", errorMsgs);
        return "/user/index.jsp";

    }
}
