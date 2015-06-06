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
			request.setAttribute("tickets", DBManager1.getTickets());

			Date dNow = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
			String date1 = ft.format(dNow);
			request.setAttribute("date1", date1);

		}

		if (pageName.endsWith("ticketview.jsp")) {
			Ticket t2 = DBManager1.getTicketById(Integer.parseInt(request
					.getParameter("ticket_id")));
			request.setAttribute("t1", t2);

			DBManager1.loadUsers();
			request.setAttribute("users", DBManager1.getUsers());

			Date dNow = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
			String date1 = ft.format(dNow);
			request.setAttribute("date1", date1);
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
		
		
	}

	public static String setString(String str, int max) {
		String str2 = str.length() > max ? str.substring(0, max) : str;
		return str2;
	};

}
