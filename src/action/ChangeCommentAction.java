package action;

import issuetracking.Comment;
import issuetracking.DBManager;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChangeCommentAction implements Action {

    @Override
    public String execute(HttpServletRequest request,
            HttpServletResponse response) {
        DBManager DBManager1 = (DBManager) request.getAttribute("dao");
        Comment comment1 = DBManager1.getCommentById(Integer.parseInt(request.getParameter("comment_id")));
        Map<String, String> errorMsgs = new HashMap<String, String>();

        comment1.setMessage(request.getParameter("message"));

        errorMsgs = comment1.validate();
        if (errorMsgs.isEmpty()) {
            DBManager1.updateComment(comment1);
        }

        request.setAttribute("errorMsgs", errorMsgs);
        return "/user/commentview.jsp";
    }

}
