package action;

import issuetracking.Component;
import issuetracking.DBManager;
import issuetracking.Ticket;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChangeTicketAction implements Action {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
                DBManager DBManager1 = (DBManager) request.getAttribute("dao");
		
		List<Component> tcomplist = new LinkedList<Component>();
		Ticket t1 = DBManager1.getTicketById(Integer.parseInt(request.getParameter("ticket_id")));
		Map<String, String> errorMsgs = new HashMap<String, String>();

                String type = request.getParameter("type");
		if (Ticket.BUG.equals(type) || Ticket.FEATURE.equals(type)) {
			String dateString = request.getParameter("date");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date= new Date();
			try {
			date = format.parse(dateString);
		
					
			} catch (ParseException e) {
				e.printStackTrace();
			}

			Ticket ticket = new Ticket(null
					, request.getParameter("title"), request.getParameter("description"), date
					, request.getParameter("author"), request.getParameter("responsible_user"), request.getParameter("type")
					, request.getParameter("state")
					);
                        ticket.setId(Integer.parseInt(request.getParameter("ticket_id")));
                        ticket.setEstimated_time(request.getParameter("estimated_time"));
                        
                        String[] compids = request.getParameterValues("compid");
			if(compids != null){
                            for(String compid: compids){
                                ticket.addComponent(DBManager1.getComponentById(compid));
                            }
			}
                        
			errorMsgs = ticket.validate(DBManager1);
			if (errorMsgs.isEmpty()) {
				DBManager1.updateTicket(ticket);
			}

		} else {
			errorMsgs.put("type", "Type not available");
		}

		request.setAttribute("errorMsgs", errorMsgs);
		return "/user/ticketview.jsp";
	}
}
