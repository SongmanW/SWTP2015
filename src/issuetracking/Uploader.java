package issuetracking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet for uploading pictures
 */
@WebServlet("/uploader")
@MultipartConfig
public class Uploader extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static String setString(String str, int max) {
        String str2 = str.length() > max ? str.substring(0, max) : str;
        return str2;
    }
    @EJB
    private DBManager DBManager1;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Uploader() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = DBManager1.getFilesPath();
        final Part filePart = request.getPart("file");
        final String fileType = getFileType(filePart);
        final String fileName = DBManager1.getNextPictureId() + "";

        OutputStream out = null;
        InputStream filecontent = null;
        final PrintWriter writer = response.getWriter();

        if (path == null) {
            writer.println("Unable to upload file, because no upload location was specified by the server admin");
        } else {
            if (fileType == null) {
                writer.println("Unsupported Filetype - Only .jpg and .png are supported");
            } else {
                PictureFile p1 = new PictureFile(DBManager1.getNextPictureId(), Integer.parseInt(request.getParameter("ticket_id")), new Date(), request.getParameter("author"), fileType);
                DBManager1.savePicture(p1);
                request.getRequestDispatcher("/user/ticketview.jsp").forward(request, response);
                try {
                    out = new FileOutputStream(new File(path + File.separator + fileName + "." + p1.getType()));
                    filecontent = filePart.getInputStream();

                    int read = 0;
                    final byte[] bytes = new byte[1024];

                    while ((read = filecontent.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }
                } catch (FileNotFoundException fne) {
                    writer.println("You either did not specify a file to upload or are "
                            + "trying to upload a file to a protected or nonexistent "
                            + "location.");
                    writer.println("<br/> ERROR: " + fne.getMessage());
                } finally {
                    if (out != null) {
                        out.close();
                    }
                    if (filecontent != null) {
                        filecontent.close();
                    }
                    if (writer != null) {
                        writer.close();
                    }
                }

            }
        }
        writer.println("<a href=\"Controller?action=preparePage&pageName=ticketview.jsp&ticket_id=" + request.getParameter("ticket_id") + "\">Back to ticket</a>");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);
    }

    private String getFileType(final Part part) {
        String s = null;
        String type = null;
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                s = content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
                type = s.substring(s.lastIndexOf('.') + 1);
            }
        }
        type = type.toLowerCase();
        switch (type) {
            case "jpg":
            case "jpeg":
            case "jpe":
                return "jpeg";
            case "png":
                return "png";
        }
        return null;
    }
}
