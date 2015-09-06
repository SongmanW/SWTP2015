package action;

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

public class ChangeSprintAction implements Action {

    @Override
    public String execute(HttpServletRequest request,
            HttpServletResponse response) {

        DBManager DBManager1 = (DBManager) request.getAttribute("dao");

        Sprint sprint1 = DBManager1.getSprintById(Integer.parseInt(request.getParameter("sprintid")));
        Map<String, String> errorMsgs = new HashMap<String, String>();

		//alten sprint aus db löschen
        //neuen sprint in db einfüllen
        //neuen sprint erschaffen
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date1 = null;
        try {
            String StartDateString = request.getParameter("Day1") + "."
                    + request.getParameter("Month1") + "."
                    + request.getParameter("Year1") + " "
                    + "00:00:00";
            date1 = format.parse(StartDateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            String EndDateString = request.getParameter("Day2") + "."
                    + request.getParameter("Month2") + "."
                    + request.getParameter("Year2") + " "
                    + "00:00:00";

            date2 = format.parse(EndDateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        sprint1.setTitle(request.getParameter("title"));
        sprint1.setStart_date(date1);
        sprint1.setEnd_date(date2);
        sprint1.setActive(false);

        //alle neu ausgewählten Tickets hinzufügen
        if (request.getParameterValues("tickids") != null) {
            for (String tickid : request.getParameterValues("tickids")) {
                Ticket temptick = DBManager1.getTicketById(Integer.parseInt(tickid));
                temptick.setSprint(sprint1);
                if (!sprint1.getTickets().contains(temptick)) {
                    sprint1.addTicket(temptick);
                }
                DBManager1.updateTicket(temptick);
            }
        }
        //alle Tickets die entfernt werden sollen auf sprintid -1 setzen.
        if (request.getParameterValues("nownosprinttickids") != null) {
            for (String tickid : request.getParameterValues("nownosprinttickids")) {
                Ticket temptick = DBManager1.getTicketById(Integer.parseInt(tickid));
                temptick.setSprint(null);
                sprint1.removeTicket(temptick);
                DBManager1.updateTicket(temptick);
            }
        }

        errorMsgs = sprint1.validate();

        if (errorMsgs.isEmpty()) {
            DBManager1.updateSprint(sprint1);
        }
        request.setAttribute("errorMsgs", errorMsgs);
        return "/user/sprints.jsp";

    }

}
