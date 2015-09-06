package action;

import issuetracking.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

	public class DeleteCommentAction implements Action {
		

		@Override
		public String execute(HttpServletRequest request,
				HttpServletResponse response) {
                        DBManager DBManager1 = (DBManager) request.getAttribute("dao");
			Comment comment1 = DBManager1.getCommentById(Integer.parseInt(request.getParameter("cid")));
			DBManager1.deleteComment(comment1);
			return "/user/ticketview.jsp";
		}

	}