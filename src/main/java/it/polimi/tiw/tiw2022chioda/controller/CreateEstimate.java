package it.polimi.tiw.tiw2022chioda.controller;

import it.polimi.tiw.tiw2022chioda.bean.Estimate;
import it.polimi.tiw.tiw2022chioda.bean.Option;
import it.polimi.tiw.tiw2022chioda.bean.Product;
import it.polimi.tiw.tiw2022chioda.bean.User;
import it.polimi.tiw.tiw2022chioda.dao.*;
import it.polimi.tiw.tiw2022chioda.utils.ConnectionHandler;
import org.apache.commons.text.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
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
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        List<String> optionCode = null;
        String productCode = null;

        optionCode = List.of(request.getParameterValues("optionCode")); // .escapeJava(request.getParameter("optionCode"));
        productCode = StringEscapeUtils.escapeJava(request.getParameter("productCode"));

        if (productCode == null ||
                optionCode.isEmpty() ||
                productCode.isEmpty()) {
            response.sendRedirect(session.getServletContext().getContextPath() + "/GoToClientHome?error=2");
            //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            //response.getWriter().println("Data fields must not be empty");
            return;
        }

        AvailabilityDAO availabilityDAO = new AvailabilityDAO(connection);
        EstimateDAO estimateDAO = new EstimateDAO(connection);
        ProductDAO productDAO = new ProductDAO(connection);

        Product actualProduct;
        try {
            actualProduct = productDAO.getByCode(Integer.parseInt(productCode));
        } catch (SQLException e) {
            response.sendRedirect(session.getServletContext().getContextPath() + "/GoToClientHome?error=1");
            //response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Error while querying database: " + e.getMessage());
            return;
        }
        List<Integer> optCodes = optionCode.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        List<Integer> possibleOptCodes;
        try {
            possibleOptCodes = availabilityDAO.getFromProduct(actualProduct.getCode());
        } catch (SQLException e){
            response.sendRedirect(session.getServletContext().getContextPath() + "/GoToClientHome?error=1");
            //response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Error while querying database: " + e.getMessage());
            return;
        }

        if(!possibleOptCodes.containsAll(optCodes)){
            response.sendRedirect(session.getServletContext().getContextPath() + "/GoToClientHome?error=2");
            //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            //response.getWriter().println("There are options that are not available for the chosen product");
            return;
        }

        Estimate candidate = new Estimate();
        candidate.setClient((User) session.getAttribute("user"));
        candidate.setProductCode(actualProduct.getCode());
        candidate.setOptionCodes(optCodes);
        try{
            estimateDAO.createEstimate(candidate, (User) session.getAttribute("user"));
        } catch (SQLException e){
            response.sendRedirect(session.getServletContext().getContextPath() + "/GoToClientHome?error=1");
            //response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Error while inserting in database: " + e.getMessage());
            return;
        }

        response.sendRedirect(session.getServletContext().getContextPath() + "/GoToClientHome?error=0");
    }
}
