/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package issuetracking;

import action.Action;
import action.ActionFactory;
import action.AdminActionFactory;
import java.io.IOException;
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

    private static final String ADMIN_JSP_PATH = "/adminpages";
    @EJB
            DBManager DBManager1;

    private ActionFactory actionFactory = AdminActionFactory.getInstance();

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
        if (action != null) {
            Action aktion = actionFactory.getActionByName(action);

            if (aktion != null) {
                request.setAttribute("dao", DBManager1);
                String result = aktion.execute(request, response);
                if (result != null) {
                    requestedPage = result;
                }
            } else {
                requestedPage = "help.jsp";
            }
        }
        preparePage(requestedPage, request, response);
        request.getRequestDispatcher(requestedPage).forward(request, response);
    }

    /**
     * Bereitet die Parameter für die entsprechende Seite vor
     *
     * @param pageName Name der aufgerufenen Seite
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void preparePage(String pageName, HttpServletRequest request,
            HttpServletResponse response) {
        if (pageName.endsWith("users.jsp")) {
            request.setAttribute("users", DBManager1.getUsers());
        }

        if (pageName.endsWith("userview.jsp")) {
            User u1 = DBManager1.getUserByUserid(request
                    .getParameter("user_id"));
            request.setAttribute("u1", u1);
        }

        if (pageName.endsWith("statistics.jsp")) {
            request.setAttribute("tickets", DBManager1.TicketCount());
            request.setAttribute("sprints", DBManager1.sprintCount());
            request.setAttribute("closedtickets", DBManager1.closedTicketCount());
            request.setAttribute("comments", DBManager1.commentCount());
            request.setAttribute("components", DBManager1.componentCount());
            request.setAttribute("users", DBManager1.userCount());
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
