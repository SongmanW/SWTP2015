package action;

import issuetracking.Component;
import issuetracking.DBManager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ChangeComponentAction implements Action {
	
	private static final DBManager DBManager1 = DBManager.getInstance();

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		Component comp1 = DBManager1.getComponentById(request.getParameter("comp_id"));
		Map<String, String> errorMsgs = new HashMap<String, String>();
		
		Component comp2 = new Component();
		comp2.setCompid(comp1.getCompid());
		comp2.setDescription(request.getParameter("description"));
		
		errorMsgs = comp2.validate();
		if(errorMsgs.isEmpty()){
			DBManager1.updateComponent(comp2);
		}
		
		request.setAttribute("errorMsgs", errorMsgs);
		return "componentview.jsp";
	}

}
