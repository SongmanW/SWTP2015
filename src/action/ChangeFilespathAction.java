package action;

import issuetracking.DBManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChangeFilespathAction implements Action {

    @Override
    public String execute(HttpServletRequest request,
            HttpServletResponse response) {
        DBManager DBManager1 = (DBManager) request.getAttribute("dao");
        String path = request.getParameter("path");
		DBManager1.setFilesPath(path);
		return "/user/index.jsp";
    }

}
