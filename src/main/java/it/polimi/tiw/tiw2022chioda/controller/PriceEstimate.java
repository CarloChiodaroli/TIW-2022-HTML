package it.polimi.tiw.tiw2022chioda.controller;

import it.polimi.tiw.tiw2022chioda.bean.Estimate;
import it.polimi.tiw.tiw2022chioda.bean.User;
import it.polimi.tiw.tiw2022chioda.dao.EstimateDAO;
import it.polimi.tiw.tiw2022chioda.enums.UserType;
import it.polimi.tiw.tiw2022chioda.utils.ConnectionHandler;
import it.polimi.tiw.tiw2022chioda.utils.ErrorSender;

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
        ErrorSender.wrongHttp(response, "Get");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pricePar = request.getParameter("price");
        String employeeIdPar = request.getParameter("employeeId");
        String estimateCodePar = request.getParameter("estimateCode");

        if (pricePar == null ||
                employeeIdPar == null ||
                estimateCodePar == null ||
                estimateCodePar.isEmpty() ||
                pricePar.isEmpty() ||
                employeeIdPar.isEmpty()){
            ErrorSender.user(response,"Requested data must be not empty");
            return;
        }

        int price;
        int employeeId;
        int estimateCode;

        try{
            price = Integer.parseInt(pricePar);
            employeeId = Integer.parseInt(employeeIdPar);
            estimateCode = Integer.parseInt(estimateCodePar);
        } catch (NumberFormatException e) {
            ErrorSender.user(response, "Some integer parameters are not integers");
            return;
        }

        User employee = (User) request.getSession().getAttribute("user");

        if (employeeId != employee.getID()){
            ErrorSender.user(response, "Gotten Id is not actual user Id");
            return;
        }

        if(employee.getUserType() != UserType.EMPLOYEE){
            ErrorSender.user(response, "Actual user has to be an Employee User");
            return;
        }

        if (price <= 0){
            ErrorSender.user(response, "Price cannot be negative");
            return;
        }

        EstimateDAO estimateDAO = new EstimateDAO(connection);
        Estimate estimate;
        try{
            estimate = estimateDAO.getByCode(estimateCode);
        } catch(SQLException e){
            ErrorSender.database(response, "getting estimate");
            return;
        }

        if(estimate == null){
            ErrorSender.user(response, "Got wrong estimate code");
            return;
        }

        try{
            estimateDAO.priceEstimate(estimate, employee, Double.parseDouble(pricePar));
        } catch(SQLException e){
            ErrorSender.database(response, "saving price");
            return;
        }

        String path = getServletContext().getContextPath() + "/GoToEmployeeHome";
        response.sendRedirect(path);
    }
}
