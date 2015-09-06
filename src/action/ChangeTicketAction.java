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

                        t1.setTitle(request.getParameter("title"));
                        t1.setDescription(request.getParameter("description"));
                        t1.setResponsible_user(request.getParameter("responsible_user"));
                        t1.setType(request.getParameter("type"));
                        t1.setStatus(request.getParameter("status"));
                        t1.setCreation_date(date);
                        t1.setEstimated_time(request.getParameter("estimated_time"));
                        String[] compids = request.getParameterValues("compid");
			if(compids != null){
                            for(String sCompid: compids){
                                Integer iCompid = Integer.parseInt(sCompid);
                                Component comp1 = DBManager1.getComponentById(iCompid);
                                t1.addComponent(comp1);
                            }
			}
                        
			errorMsgs = t1.validate(DBManager1);
			if (errorMsgs.isEmpty()) {
				DBManager1.updateTicket(t1);
			}

		} else {
			errorMsgs.put("type", "Type not available");
		}

		request.setAttribute("errorMsgs", errorMsgs);
		return "/user/ticketview.jsp";
	}
}
