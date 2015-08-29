package action;

import issuetracking.*;

import java.text.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddSprintAction implements Action{
	private static final DBManager DBManager1 = DBManager.getInstance();

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> errorMsgs = new HashMap<String, String>();
		//build Sprint
		DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Date date1=null;
		try {
			String StartDateString= request.getParameter("d1") + "."+
					 request.getParameter("m1")  + "."+
					  request.getParameter("y1") + " "+
					   "00:00:00";
			date1 = format.parse(StartDateString);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date date2=null;
		try {
			String EndDateString= request.getParameter("d2") + "."+
					 request.getParameter("m2")  + "."+
					  request.getParameter("y2") + " "+
					  "00:00:00";
			
			date2 = format.parse(EndDateString);
		
		} catch (ParseException e) {
			e.printStackTrace();
		}

		
		Sprint sprint1 = new Sprint(DBManager1.getNextSprintId(),  request.getParameter("title"), date1,date2, false);
		
		//validate and save in DB
		errorMsgs = sprint1.validate();
		if(errorMsgs.isEmpty()){
			DBManager1.saveSprint(sprint1);
			
			//checked tickets now belong to the sprint
			if(request.getParameterValues("tickids")!=null){
				
			for(String tickid: request.getParameterValues("tickids")){
				Ticket temptick = DBManager1.getTicketById(Integer.parseInt(tickid));
				temptick.setSprintid(sprint1.getSprintid());
				DBManager1.updateTicket(temptick);
			}}
			
		}
		request.setAttribute("errorMsgs", errorMsgs);
		return "user/sprints.jsp";
		
		
	}

}