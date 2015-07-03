package action;

import issuetracking.Component;
import issuetracking.DBManager;
import issuetracking.TicketBug;
import issuetracking.TicketFeature;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddTicketAction implements Action{
	private static final DBManager DBManager1 = DBManager.getInstance();

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		Component tempcomp;
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

			TicketBug tbug = new TicketBug(DBManager1.getNextTicketId(), Integer.parseInt(request.getParameter("sprintid"))
					, request.getParameter("title"), request.getParameter("description"), date
					, request.getParameter("author"), request.getParameter("responsible_user"), request.getParameter("type")
					, request.getParameter("state")
					);
			
			
			
			errorMsgs = tbug.validate();
			
			if (errorMsgs.isEmpty()) {
				DBManager1.saveTicket(tbug);
				for(String compid: request.getParameterValues("compid")){
					tempcomp = DBManager1.getComponentById(compid);
					DBManager1.saveTCRelation(tbug, tempcomp);
				}
			}
		} else
		if (request.getParameter("type").equals("feature")) {
			

			
			String dateString = request.getParameter("date");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date= new Date();
			try {
			date = format.parse(dateString);
		
					
			} catch (ParseException e) {
				e.printStackTrace();
			}

			TicketFeature tfeature = new TicketFeature(DBManager1.getNextTicketId(), Integer.parseInt(request.getParameter("sprintid"))
					, request.getParameter("title"), request.getParameter("description"), date
					, request.getParameter("author"), request.getParameter("responsible_user"), request.getParameter("type")
					, request.getParameter("state"), request.getParameter("estimated_time")
					);
					
					
			
			errorMsgs = tfeature.validate();
			if (errorMsgs.isEmpty()) {
				DBManager1.saveTicket(tfeature);
				for(String compid: request.getParameterValues("compid")){
					tempcomp = DBManager1.getComponentById(compid);
					DBManager1.saveTCRelation(tfeature, tempcomp);
				}
			}

		}else{
			errorMsgs.put("type", "Type not available");
		}
		
		request.setAttribute("errorMsgs", errorMsgs);
		return "index.jsp";

	}
}