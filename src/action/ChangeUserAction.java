package action;

import issuetracking.DBManager;
import issuetracking.User;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChangeUserAction implements Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        DBManager DBManager1 = (DBManager) request.getAttribute("dao");
        User u1 = DBManager1.getUserByUserid(request.getParameter("user_id"));

        User u1Update = new User(u1.getUserid(), request.getParameter("passwordinput"));

        Map<String, String> errorMsgs = new HashMap<String, String>();
        errorMsgs = User.validateUserChange(request.getParameter("passwordinput"));

        if (errorMsgs.isEmpty()) {
            DBManager1.updateUser(u1Update);
        }

        request.setAttribute("errorMsgs", errorMsgs);
        if ("changeUser_from_account".equals(request.getParameter("action"))) {
            return "/user/userpage.jsp";
        } else {
            return "/admin/userview.jsp";
        }
    }
}
