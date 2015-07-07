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
		
		DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Date date1=null;
		try {
			String StartDateString= request.getParameter("d1") + "."+
					 request.getParameter("m1")  + "."+
					  request.getParameter("y1") + " "+
					   "00:00:00";
			System.out.println(StartDateString);		    
			date1 = format.parse(StartDateString);
			System.out.println(date1.toString());
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
		errorMsgs = sprint1.validate();
		if(errorMsgs.isEmpty()){
			DBManager1.saveSprint(sprint1);
			if(request.getParameterValues("tickids")!=null){
			for(String tickid: request.getParameterValues("tickids")){
				Ticket temptick = DBManager1.getTicketById(Integer.parseInt(tickid));
				System.out.println("In addsprintaction:"+tickid);
			//	DBManager1.FillTicketsSprintfield(temptick, sprintid);
			}}
			
		}
		request.setAttribute("errorMsgs", errorMsgs);
		return "sprints.jsp";
		
		
	}

}