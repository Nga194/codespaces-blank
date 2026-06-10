package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


@WebServlet("/convert")
public class ChuyendoiSevlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        double km = Double.parseDouble(request.getParameter("km"));

        double miles = km * 0.621371;

        request.setAttribute("result", miles);

        request.getRequestDispatcher("result.jsp")
               .forward(request, response);
    }

    protected void doPost1(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        double km = Double.parseDouble(request.getParameter("km"));

        double miles = km * 0.621371;

        
        response.setContentType("text/html;charset=UTF-8");
        try
        {
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Kết quả chuyển đổi</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet có địa chỉ: " + request.getContextPath() + "</h1>");
        out.println("<h2>Kết quả: " + miles + " miles</h2>");
        out.println("</body>");
        out.println("</html>");

        }
        catch(Exception ex)
        {


        }
    }
}