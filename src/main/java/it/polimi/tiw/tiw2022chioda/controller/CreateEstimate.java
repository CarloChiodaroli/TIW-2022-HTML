package it.polimi.tiw.tiw2022chioda.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "CreateEstimate", value = "/CreateEstimate")
public class CreateEstimate extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("Received correctly");
        HttpSession session = request.getSession();
        response.sendRedirect(session.getServletContext().getContextPath() + "/GoToClientHome");
    }
}
