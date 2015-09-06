package action;

import issuetracking.Component;
import issuetracking.DBManager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ChangeComponentAction implements Action {
	

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
                DBManager DBManager1 = (DBManager) request.getAttribute("dao");
                Integer compid = Integer.parseInt(request.getParameter("compid"));
		Component comp1 = DBManager1.getComponentById(compid);
		Map<String, String> errorMsgs = new HashMap<String, String>();
		
                comp1.setName(request.getParameter("name"));
		comp1.setDescription(request.getParameter("description"));
		errorMsgs = comp1.validate();
		if(errorMsgs.isEmpty()){
			DBManager1.updateComponent(comp1);
		}
		
		request.setAttribute("errorMsgs", errorMsgs);
		return "/user/componentview.jsp";
	}

}
