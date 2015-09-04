package action;

import issuetracking.DBManager;
import issuetracking.Sprint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EndSprintAction implements Action{
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
                DBManager DBManager1 = (DBManager) request.getAttribute("dao");
		Sprint sprint = DBManager1.getSprintById(Integer.parseInt(request.getParameter("sprint_id")));
		Sprint update = new Sprint(sprint.getSprintid(), sprint.getTitle(), sprint.getStart_date(),sprint.getEnd_date(),false);
		
		DBManager1.updateSprint(update);
	
		return "user/sprints.jsp";
	}

}
