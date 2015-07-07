package action;

import issuetracking.Component;
import issuetracking.DBManager;
import issuetracking.Ticket;
import issuetracking.TicketBug;
import issuetracking.TicketFeature;

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
	private static final DBManager DBManager1 = DBManager.getInstance();

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		
		List<Component> tcomplist = new LinkedList<Component>();
		Ticket t1 = DBManager1.getTicketById(Integer.parseInt(request.getParameter("ticket_id")));
		Map<String, String> errorMsgs = new HashMap<String, String>();

		if (request.getParameter("type").equals("bug")) {
			String dateString = request.getParameter("date");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date= new Date();
			try {
			date = format.parse(dateString);
		
					
			} catch (ParseException e) {
				e.printStackTrace();
			}

			TicketBug tbug = new TicketBug(Integer.parseInt(request.getParameter("ticket_id")), -1/*Integer.parseInt(request.getParameter("sprintid"))*/
					, request.getParameter("title"), request.getParameter("description"), date
					, request.getParameter("author"), request.getParameter("responsible_user"), request.getParameter("type")
					, request.getParameter("state")
					);
			errorMsgs = tbug.validate();
			if (errorMsgs.isEmpty()) {
				DBManager1.updateTicket(tbug);
				String[] compids = request.getParameterValues("compid");
				if(compids != null){
					for(String compid: compids){
						tcomplist.add(DBManager1.getComponentById(compid));
					}
				}
				DBManager1.updateTCRelation(t1, tcomplist);
			}

		} else if (request.getParameter("type").equals("feature")) {
			String dateString = request.getParameter("date");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date= new Date();
			try {
			date = format.parse(dateString);		
			} catch (ParseException e) {
				e.printStackTrace();
			}

			TicketFeature tfeature = new TicketFeature(Integer.parseInt(request.getParameter("ticket_id")), -1/*Integer.parseInt(request.getParameter("sprintid"))*/
					, request.getParameter("title"), request.getParameter("description"), date
					, request.getParameter("author"), request.getParameter("responsible_user"), request.getParameter("type")
					, request.getParameter("state"), request.getParameter("estimated_time")
					);

			errorMsgs = tfeature.validate();
			if (errorMsgs.isEmpty()) {
				DBManager1.updateTicket(tfeature);
				String[] compids = request.getParameterValues("compid");
				if(compids != null){
					for(String compid: compids){
						tcomplist.add(DBManager1.getComponentById(compid));
					}
				}
				DBManager1.updateTCRelation(t1, tcomplist);
			}
		} else {
			errorMsgs.put("type", "Type not available");
		}

		request.setAttribute("errorMsgs", errorMsgs);
		return "ticketview.jsp";
	}
}
