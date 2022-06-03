package it.polimi.tiw.tiw2022chioda.controller;

import it.polimi.tiw.tiw2022chioda.bean.Estimate;
import it.polimi.tiw.tiw2022chioda.bean.Product;
import it.polimi.tiw.tiw2022chioda.bean.User;
import it.polimi.tiw.tiw2022chioda.dao.*;
import it.polimi.tiw.tiw2022chioda.utils.ConnectionHandler;
import it.polimi.tiw.tiw2022chioda.utils.ErrorSender;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "CreateEstimate", value = "/CreateEstimate")
public class CreateEstimate extends HttpServlet {

    private Connection connection;

    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ErrorSender.wrongHttp(response, "Get");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String[] optionCodes = request.getParameterValues("optionCode");
        String productCodePar = StringEscapeUtils.escapeJava(request.getParameter("productCode"));

        if (productCodePar == null ||
                optionCodes == null ||
                productCodePar.isEmpty()) {
            ErrorSender.userWrongData(response, "The form has not been completely filled");
            return;
        }

        List<String> optionCode = List.of(optionCodes);

        AvailabilityDAO availabilityDAO = new AvailabilityDAO(connection);
        EstimateDAO estimateDAO = new EstimateDAO(connection);
        ProductDAO productDAO = new ProductDAO(connection);

        Product actualProduct = null;

        int productCode;

        try {
            productCode = Integer.parseInt(productCodePar);
        } catch (NumberFormatException e) {
            ErrorSender.userNotNumber(response);
            return;
        }

        try {
            actualProduct = productDAO.getByCode(productCode);
        } catch (SQLException e) {
            ErrorSender.database(response);
            return;
        }

        if(actualProduct == null){
            ErrorSender.userWrongData(response, "A valid product ID must be given");
            return;
        }

        List<Integer> optCodes;
        try {
            optCodes = optionCode.stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch(NumberFormatException e) {
            productCode = Integer.parseInt(productCodePar);
            return;
        }

        if(optCodes.isEmpty()){
            ErrorSender.userWrongData(response, "At leas one valid option code must be given");
            return;
        }

        List<Integer> possibleOptCodes;
        try {
            possibleOptCodes = availabilityDAO.getFromProduct(actualProduct.getCode());
        } catch (SQLException e){
            ErrorSender.database(response);
            return;
        }

        if(!possibleOptCodes.containsAll(optCodes)){
            ErrorSender.userWrongData(response, "Some options are not available for the selected product");
            return;
        }

        Estimate candidate = new Estimate();
        candidate.setClient((User) session.getAttribute("user"));
        candidate.setProductCode(actualProduct.getCode());
        candidate.setOptionCodes(optCodes);

        try{
            estimateDAO.createEstimate(candidate, (User) session.getAttribute("user"));
        } catch (SQLException e){
            ErrorSender.database(response);
            return;
        }

        response.sendRedirect(session.getServletContext().getContextPath() + "/GoToClientHome");
    }
}
