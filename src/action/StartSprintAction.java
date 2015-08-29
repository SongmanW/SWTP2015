package action;

import issuetracking.DBManager;
import issuetracking.Sprint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StartSprintAction implements Action{
	private static final DBManager DBManager1 = DBManager.getInstance();
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		Sprint sprint = DBManager1.getSprintById(Integer.parseInt(request.getParameter("sprint_id")));
		
		if(DBManager1.getActiveSprint() == null){
			sprint.setActive(true);
			DBManager1.updateSprint(sprint);
		}
		
		return "sprints.jsp";
	}

}
