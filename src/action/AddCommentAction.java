package action;

import issuetracking.*;
import java.text.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddCommentAction implements Action{
	private static final DBManager DBManager1 = DBManager.getInstance();

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, String> errorMsgs = new HashMap<String, String>();
		
		DateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
		Date date=null;
		try {
			date = format.parse(request.getParameter("date"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Comment comment1 = new Comment(DBManager1.getNextCommentId(), Integer.parseInt(request.getParameter("ticket_id")), date,request.getParameter("author"), request.getParameter("message"));

		errorMsgs = comment1.validate();
		
		if(errorMsgs.isEmpty()){
			DBManager1.saveComment(comment1);
		}
		
		request.setAttribute("errorMsgs", errorMsgs);
		return "ticketview.jsp";
	}

}