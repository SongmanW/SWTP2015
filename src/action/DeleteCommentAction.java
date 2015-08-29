package action;

import issuetracking.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

	public class DeleteCommentAction implements Action {
		
		private static final DBManager DBManager1 = DBManager.getInstance();

		@Override
		public String execute(HttpServletRequest request,
				HttpServletResponse response) {
			Comment comment1 = DBManager1.getCommentById(Integer.parseInt(request.getParameter("cid")));
			DBManager1.deleteComment(comment1);
			return "user/ticketview.jsp";
		}

	}