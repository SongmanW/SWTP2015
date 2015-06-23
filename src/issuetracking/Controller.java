package issuetracking;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;

/**
 * Servlet implementation class Controller
 */
@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static final DBManager DBManager1 = DBManager.getInstance();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Controller() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		
		
		String action = request.getParameter("action");

		Action aktion = Action.actionFactory(action);
		
		if (aktion != null) {
			String result = aktion.execute(request, response);
			if (result != null) {
				preparePage(result, request, response);
				request.getRequestDispatcher(result).forward(request, response);
			}
		} else
		request.getRequestDispatcher("help.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

	
	public void preparePage(String pageName, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		DBManager DBManager1 = DBManager.getInstance();

		if (pageName.endsWith("index.jsp")) {
			request.setAttribute("users", DBManager1.getUsers());
			request.setAttribute("tickets_open", DBManager1.getTicketsByState("open"));
			request.setAttribute("tickets_closed", DBManager1.getTicketsByState("closed"));
			request.setAttribute("tickets_inprogress", DBManager1.getTicketsByState("in_progress"));
			request.setAttribute("tickets_test", DBManager1.getTicketsByState("test"));
			
			Date dNow = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
			String date1 = ft.format(dNow);
			request.setAttribute("date1", date1);

			DBManager1.loadComponents();
			request.setAttribute("compids", DBManager1.getComponents());
		}

		if (pageName.endsWith("ticketview.jsp")) {
			Ticket t2 = DBManager1.getTicketById(Integer.parseInt(request.getParameter("ticket_id")));
			request.setAttribute("t1", t2);

			DBManager1.loadUsers();
			request.setAttribute("users", DBManager1.getUsers());

			Date dNow = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
			String date1 = ft.format(dNow);
			request.setAttribute("date1", date1);
			
			DBManager1.loadComponents();
			request.setAttribute("ticket_compids", DBManager1.getComponentsByTicket(
					Integer.parseInt(request.getParameter("ticket_id"))));
			request.setAttribute("compids", DBManager1.getComponents());
			
			DBManager1.loadComments();
			request.setAttribute("ticket_comments", DBManager1.getCommentsByTicket(
					Integer.parseInt(request.getParameter("ticket_id"))));

			SimpleDateFormat ft2 = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
			String date2 = ft2.format(dNow);
			request.setAttribute("date2", date2);
			
			
			for(Comment com1 : DBManager1.getCommentsByTicket(
					Integer.parseInt(request.getParameter("ticket_id")))) {
			}
			
		}

		if(pageName.endsWith("users.jsp")){
			request.setAttribute("users", DBManager1.getUsers());
		}

		if (pageName.endsWith("userview.jsp")) {
			User u1 = DBManager1.getUserByUserid(request
					.getParameter("user_id"));
			request.setAttribute("u1", u1);
		}
		if (pageName.endsWith("userpage.jsp")) {
			User u1 = DBManager1.getUserByUserid(request.getParameter("user_id"));
			request.setAttribute("u1", u1);
		}
		if (pageName.endsWith("login.jsp")) {

		}
		
		if(pageName.endsWith("components.jsp")){
			request.setAttribute("users", DBManager1.getUsers());
			request.setAttribute("components", DBManager1.getComponents());
		}
		
		if(pageName.endsWith("componentview.jsp")){
			Component c1 = DBManager1.getComponentById(request.getParameter("compid"));
			request.setAttribute("c1", c1);
		}
		if(pageName.endsWith("commentview.jsp")){
			Comment comment1 = DBManager1.getCommentById(Integer.parseInt(request.getParameter("comment_id"))); 
			request.setAttribute("c1", comment1);
		}
	
		
	}

	public static String setString(String str, int max) {
		String str2 = str.length() > max ? str.substring(0, max) : str;
		return str2;
	};

}
