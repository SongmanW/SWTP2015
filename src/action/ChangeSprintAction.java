package action;

import issuetracking.Component;
import issuetracking.DBManager;
import issuetracking.Sprint;
import issuetracking.Ticket;

import java.awt.List;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChangeSprintAction implements Action{


	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		
                DBManager DBManager1 = (DBManager) request.getAttribute("dao");
		
		Sprint sprint1 = DBManager1.getSprintById(Integer.parseInt(request.getParameter("sprintid")));
		Map<String, String> errorMsgs = new HashMap<String, String>();
		
		
		
		
		//alten sprint aus db l�schen
		//neuen sprint in db einf�llen

		//neuen sprint erschaffen
		DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Date date1=null;
		try {
			String StartDateString= request.getParameter("Day1") + "."+
					 request.getParameter("Month1")  + "."+
					  request.getParameter("Year1") + " "+
					   "00:00:00";
			date1 = format.parse(StartDateString);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date date2=null;
		try {
			String EndDateString= request.getParameter("Day2") + "."+
					 request.getParameter("Month2")  + "."+
					  request.getParameter("Year2") + " "+
					  "00:00:00";
			
			date2 = format.parse(EndDateString);
		
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		Sprint sprint2 = new Sprint(Integer.parseInt(request.getParameter("sprintid")),  request.getParameter("title"), date1,date2, false);
		errorMsgs = sprint2.validate();


		if(errorMsgs.isEmpty()){
			
			//alle Tickets des alten Sprints updaten sodass sie zum neuen Sprint geh�ren
			LinkedList<Ticket> tList=(LinkedList<Ticket>) DBManager1.getTicketsByState("beliebig", sprint1.getSprintid());
			if( tList!=null&&!(tList.isEmpty())){
				for(Ticket ticket1: tList){
					Ticket temptick = ticket1;
					temptick.setSprintid(sprint2.getSprintid());
					DBManager1.updateTicket(temptick);
				}}
			
			//alle neu ausgew�hlten Tickets hinzuf�gen
			if(request.getParameterValues("tickids")!=null){
			for(String tickid: request.getParameterValues("tickids")){
				Ticket temptick = DBManager1.getTicketById(Integer.parseInt(tickid));
				temptick.setSprintid(sprint2.getSprintid());
				DBManager1.updateTicket(temptick);
			}}
			//alle Tickets die entfernt werden sollen auf sprintid -1 setzen.
			if(request.getParameterValues("nownosprinttickids")!=null){
				for(String tickid: request.getParameterValues("nownosprinttickids")){
					Ticket temptick = DBManager1.getTicketById(Integer.parseInt(tickid));
					temptick.setSprintid(-1);
					DBManager1.updateTicket(temptick);
				}}
			
			
			//neuen sprint in db einf�llen(alten sprint aus db l�schen)
			DBManager1.deleteSprint(sprint1);
			DBManager1.saveSprint(sprint2);
		}
		request.setAttribute("errorMsgs", errorMsgs);
		return "user/sprints.jsp";


	}

	
}
