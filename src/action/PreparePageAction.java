package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PreparePageAction implements Action{

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		return request.getParameter("pageName");
	}
}
