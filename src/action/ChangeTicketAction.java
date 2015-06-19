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
			TicketBug tbug = new TicketBug();
			tbug.setId(t1.getId());
			tbug.setTitle(request.getParameter("title"));
			tbug.setDescription(request.getParameter("description"));
			String dateString = request.getParameter("date");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			try {
				date = format.parse(dateString);
				tbug.setDate(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			tbug.setAuthor(request.getParameter("author"));
			tbug.setResponsible_user(request.getParameter("responsible_user"));
			tbug.setType(request.getParameter("type"));
			tbug.setState(request.getParameter("state"));
			errorMsgs = tbug.validate();
			if (errorMsgs.isEmpty()) {
				DBManager1.updateTicket(tbug);
				for(String compid: request.getParameterValues("compid")){
					tcomplist.add(DBManager1.getComponentById(compid));
				}
				DBManager1.updateTCRelation(t1, tcomplist);
			}

		} else if (request.getParameter("type").equals("feature")) {
			TicketFeature tfeature = new TicketFeature();

			tfeature.setId(t1.getId());
			tfeature.setTitle(request.getParameter("title"));
			tfeature.setDescription(request.getParameter("description"));
			String dateString = request.getParameter("date");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			try {
				date = format.parse(dateString);
				tfeature.setDate(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			tfeature.setAuthor(request.getParameter("author"));
			tfeature.setResponsible_user(request
					.getParameter("responsible_user"));
			tfeature.setType(request.getParameter("type"));
			tfeature.setState(request.getParameter("state"));
			tfeature.setEstimated_time(request.getParameter("estimated_time"));

			errorMsgs = tfeature.validate();
			if (errorMsgs.isEmpty()) {
				DBManager1.updateTicket(tfeature);
				for(String compid: request.getParameterValues("compid")){
					tcomplist.add(DBManager1.getComponentById(compid));
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
