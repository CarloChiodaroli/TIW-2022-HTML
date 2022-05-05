package it.polimi.tiw.tiw2022chioda.controller;

import it.polimi.tiw.tiw2022chioda.bean.Estimate;
import it.polimi.tiw.tiw2022chioda.bean.User;
import it.polimi.tiw.tiw2022chioda.dao.EstimateDAO;
import it.polimi.tiw.tiw2022chioda.dao.UserDAO;
import it.polimi.tiw.tiw2022chioda.enums.UserType;
import it.polimi.tiw.tiw2022chioda.utils.ConnectionHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "PriceEstimate", value = "/PriceEstimate")
public class PriceEstimate extends HttpServlet {

    private Connection connection;

    public void init() throws ServletException {
        System.out.println("CheckSignUp initialization");
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String price = request.getParameter("price");
        String employeeId = request.getParameter("employeeId");
        String estimateCode = request.getParameter("estimateCode");

        if (price == null ||
                employeeId == null ||
                estimateCode == null ||
                estimateCode.isEmpty() ||
                price.isEmpty() ||
                employeeId.isEmpty()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Requested data must be not empty");
            return;
        }

        if (Double.parseDouble(price) < 0){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Price cannot be negative");
            return;
        }

        EstimateDAO estimateDAO = new EstimateDAO(connection);
        Estimate estimate;
        try{
            estimate = estimateDAO.getByCode(Integer.parseInt(estimateCode));
        } catch(SQLException e){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while getting data from database 1");
            return;
        }

        UserDAO userDAO = new UserDAO(connection);
        User employee;
        try{
            employee = userDAO.getById(Integer.parseInt(employeeId));
        } catch (SQLException e){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while getting data from database 2");
            return;
        }

        if(!employee.getUserType().equals(UserType.EMPLOYEE)){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,  "Gotten user ID is not of an employee user");
            return;
        }

        try{
            estimateDAO.priceEstimate(estimate, employee, Double.parseDouble(price));
        } catch(SQLException e){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while getting data from database 3");
            return;
        }

        String path = getServletContext().getContextPath() + "/GoToHome";
        response.sendRedirect(path);
    }
}
