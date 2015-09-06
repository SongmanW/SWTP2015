package action;

import issuetracking.Component;
import issuetracking.DBManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteComponentAction implements Action {
	

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
                DBManager DBManager1 = (DBManager) request.getAttribute("dao");
                Integer compid = Integer.parseInt(request.getParameter("comp_id"));
		Component comp = DBManager1.getComponentById(compid);
		DBManager1.deleteComponent(comp);
		return "/user/components.jsp";
	}

}
