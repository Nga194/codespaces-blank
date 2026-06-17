package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8"); // đặt TRƯỚC getWriter
        PrintWriter out = response.getWriter();

        try {
            String user = request.getParameter("user");
            String pass = request.getParameter("pass");

            System.out.println("user:" + user + " pass:" + pass);

            if ("nga".equalsIgnoreCase(user) && "123".equals(pass)) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head><meta charset='UTF-8'><title>Login thành công</title></head>");
                out.println("<body><h1>Chào nga</h1></body>");
                out.println("</html>");
            } else {
                response.sendRedirect("login.html");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}