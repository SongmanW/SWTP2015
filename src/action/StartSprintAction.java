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
		sprint.setActive(true);
		
                //deactive probably already active sprint
                Sprint active;
		if((active = DBManager1.getActiveSprint()) != null){
                    active.setActive(false);
                    DBManager1.updateSprint(active);
		}
                DBManager1.updateSprint(sprint);
		
		return "/user/sprints.jsp";
	}

}
