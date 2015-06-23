package action;

import issuetracking.Component;
import issuetracking.DBManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteComponentAction implements Action {
	
	private static final DBManager DBManager1 = DBManager.getInstance();

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		Component comp = DBManager1.getComponentById(request.getParameter("comp_id"));
		DBManager1.deleteComponent(comp);
		return "components.jsp";
	}

}
