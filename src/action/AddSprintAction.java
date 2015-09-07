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

public class AddSprintAction implements Action {

    @Override
    public String execute(HttpServletRequest request,
            HttpServletResponse response) {
        DBManager manager = (DBManager) request.getAttribute("dao");
        Map<String, String> errorMsgs = new HashMap<String, String>();
        //build Sprint
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date1 = null;
        try {
            String StartDateString = request.getParameter("d1") + "."
                    + request.getParameter("m1") + "."
                    + request.getParameter("y1") + " "
                    + "00:00:00";
            date1 = format.parse(StartDateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            String EndDateString = request.getParameter("d2") + "."
                    + request.getParameter("m2") + "."
                    + request.getParameter("y2") + " "
                    + "00:00:00";

            date2 = format.parse(EndDateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Sprint sprint1 = new Sprint(request.getParameter("title"), date1, date2, false);
        
        //validate and save in DB
        errorMsgs = sprint1.validate();
        if (errorMsgs.isEmpty()) {
            manager.saveSprint(sprint1);
        }

        if (request.getParameterValues("tickids") != null) {
            for (String tickid : request.getParameterValues("tickids")) {
                Ticket temptick = manager.getTicketById(Integer.parseInt(tickid));
                sprint1.addTicket(temptick);
                temptick.setSprint(sprint1);
            }
        }
        manager.updateSprint(sprint1);

        request.setAttribute("errorMsgs", errorMsgs);
        return "/user/sprints.jsp";

    }

}
