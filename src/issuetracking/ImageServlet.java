package issuetracking;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for showing images
 */
@WebServlet("/image")
public class ImageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    @EJB
    private DBManager DBManager1;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int fileid = Integer.parseInt(request.getParameter("file"));
        PictureFile p = DBManager1.getPictureById(fileid);
        switch (p.getType()) {
            case "jpeg":
                response.setContentType("image/jpeg");
                break;
            case "png":
                response.setContentType("image/png");
                break;
        }
        ServletOutputStream out;
        out = response.getOutputStream();
        FileInputStream fin = new FileInputStream(DBManager.getFilesPath() + File.separator + p.getPictureId() + "." + p.getType());

        BufferedInputStream bin = new BufferedInputStream(fin);
        BufferedOutputStream bout = new BufferedOutputStream(out);
        int ch = 0;;
        while ((ch = bin.read()) != -1) {
            bout.write(ch);
        }

        bin.close();
        fin.close();
        bout.close();
        out.close();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);
    }

}
