package action;

import issuetracking.Component;
import issuetracking.DBManager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddComponentAction implements Action{
	private static final DBManager DBManager1 = DBManager.getInstance();

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, String> errorMsgs = new HashMap<String, String>();
		Component comp = new Component();
		String compidinput = request.getParameter("comp_id");
		String descriptioninput = request.getParameter("description");
		
		comp.setCompid(compidinput);
		comp.setDescription(descriptioninput);
		
		errorMsgs = comp.validate();
		
		if(errorMsgs.isEmpty()){
			DBManager1.saveComponent(compidinput, descriptioninput);
		}
		
		request.setAttribute("errorMsgs", errorMsgs);
		return "components.jsp";
	}

}
