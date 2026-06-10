package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


@WebServlet("/convert2")
public class ChuyendoiSevlet2 extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        double km = Double.parseDouble(request.getParameter("km"));

        double miles = km * 0.621371;

        request.setAttribute("result", miles);

        request.getRequestDispatcher("chuyendoi2.jsp")
               .forward(request, response);
    }

    
}