package it.polimi.tiw.tiw2022chioda.controller;

import it.polimi.tiw.tiw2022chioda.bean.Estimate;
import it.polimi.tiw.tiw2022chioda.bean.Option;
import it.polimi.tiw.tiw2022chioda.bean.Product;
import it.polimi.tiw.tiw2022chioda.bean.User;
import it.polimi.tiw.tiw2022chioda.dao.*;
import it.polimi.tiw.tiw2022chioda.enums.UserType;
import it.polimi.tiw.tiw2022chioda.utils.ConnectionHandler;
import it.polimi.tiw.tiw2022chioda.utils.ErrorSender;
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
import java.io.Serial;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "EstimateDetail", value = "/EstimateDetail")
public class EstimateDetail extends HttpServlet {

    private static final String productDetailPath = "WEB-INF/simpleDetail.html";
    private static final String priceEstimatePath = "WEB-INF/priceEstimate.html";

    @Serial
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    private Connection connection = null;

    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        EstimateDAO estimateDAO = new EstimateDAO(connection);
        ProductDAO productDAO = new ProductDAO(connection);
        OptionDAO optionDAO = new OptionDAO(connection);
        UserDAO userDAO = new UserDAO(connection);
        DecorDAO decorDAO = new DecorDAO(connection);

        User user = (User) session.getAttribute("user");
        String tmpEstCode = request.getParameter("estimateCode");

        boolean priceEstimatePage = false;
        boolean pricedEstimate = false;
        if (tmpEstCode == null || tmpEstCode.isEmpty()) {
            ErrorSender.user(response, "Got no Estimate Code");
            return;
        }
        int estimateCode = Integer.parseInt(request.getParameter("estimateCode"));
        Estimate baseEstimate;
        try {
            baseEstimate = estimateDAO.getByCode(estimateCode);
        } catch (SQLException e) {
            ErrorSender.server(response);
            return;
        }
        if(baseEstimate == null){
            ErrorSender.user(response, "There is no estimate with gotten code");
            return;
        }
        pricedEstimate = !(baseEstimate.getEmployeeId() == 0);
        if (!(baseEstimate.getClientId() == user.getID()) && !(baseEstimate.getEmployeeId() == user.getID())) {
            if (user.getUserType().equals(UserType.EMPLOYEE) && baseEstimate.getPrice() == 0) {
                // decide to go to price estimate page
                priceEstimatePage = true;
            } else {
                ErrorSender.user(response, "User cannot see this estimate's details");
                return;
            }
        }
        List<Integer> optionCodes;
        try {
            optionCodes = decorDAO.getOptionCodesFromEstimateCode(estimateCode);
        } catch (SQLException e) {
            ErrorSender.server(response);
            return;
        }
        baseEstimate.setOptionCodes(optionCodes);
        List<Option> options = new ArrayList<>();
        for (int optionCode : baseEstimate.getOptionCodes()) {
            try {
                options.add(optionDAO.getFromCode(optionCode));
            } catch (SQLException e) {
                ErrorSender.server(response);
                return;
            }
        }

        Product product;
        try {
            product = productDAO.getByCode(baseEstimate.getProductCode());
        } catch (SQLException e) {
            ErrorSender.server(response);
            return;
        }

        Optional<User> employee;
        User client;
        if(baseEstimate.getClientId() == user.getID()){
            client = user;
            try {
                employee = Optional.ofNullable(userDAO.getById(baseEstimate.getEmployeeId()));
            } catch (SQLException e) {
                ErrorSender.server(response);
                return;
            }
        } else {
            employee = Optional.of(user);
            try {
                client = userDAO.getById(baseEstimate.getClientId());
            } catch (SQLException e) {
                ErrorSender.server(response);
                return;
            }
        }

        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("product", product);
        ctx.setVariable("estimate", baseEstimate);
        ctx.setVariable("options", options);
        ctx.setVariable("canPrice", priceEstimatePage);
        ctx.setVariable("client", client);
        ctx.setVariable("priced", pricedEstimate);
        ctx.setVariable("employee", employee.orElse(null));
        if(priceEstimatePage) templateEngine.process(priceEstimatePath, ctx, response.getWriter());
        else templateEngine.process(productDetailPath, ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
