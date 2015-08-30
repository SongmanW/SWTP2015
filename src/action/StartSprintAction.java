package action;

import issuetracking.DBManager;
import issuetracking.Sprint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StartSprintAction implements Action{
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
                DBManager DBManager1 = (DBManager) request.getAttribute("dao");
		Sprint sprint = DBManager1.getSprintById(Integer.parseInt(request.getParameter("sprint_id")));
		
		if(DBManager1.getActiveSprint() == null){
			sprint.setActive(true);
			DBManager1.updateSprint(sprint);
		}
		
		return "user/sprints.jsp";
	}

}
