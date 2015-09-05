/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package issuetracking;

import action.Action;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author erfier
 */
public class AdminController extends HttpServlet {
    
    @EJB
    DBManager DBManager1;
    private static final String ADMIN_JSP_PATH = "/adminpages";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String requestedPage = ADMIN_JSP_PATH.concat(request.getPathInfo()).replaceAll(request.getQueryString(), "");
	String action = request.getParameter("action");
        if(action != null){
            Action aktion = Action.actionFactory(action);

            if (aktion != null) {
                request.setAttribute("dao", DBManager1);
                String result = aktion.execute(request, response);
                if (result != null) {
                    requestedPage = result;
                }
            } else
                requestedPage = "help.jsp";
        }
        preparePage(requestedPage, request, response);
        request.getRequestDispatcher(requestedPage).forward(request, response);
    }
    
    	/**
	 * Bereitet die Parameter für die entsprechende Seite vor
	 * @param pageName Name der aufgerufenen Seite
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void preparePage(String pageName, HttpServletRequest request,
			HttpServletResponse response){
            	if(pageName.endsWith("users.jsp")){
			request.setAttribute("users", DBManager1.getUsers());
		} 

		if (pageName.endsWith("userview.jsp")) {
			User u1 = DBManager1.getUserByUserid(request
					.getParameter("user_id"));
			request.setAttribute("u1", u1);
		}
        }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
