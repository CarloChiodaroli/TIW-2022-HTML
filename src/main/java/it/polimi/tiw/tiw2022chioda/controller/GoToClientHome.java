package it.polimi.tiw.tiw2022chioda.controller;

import it.polimi.tiw.tiw2022chioda.bean.Estimate;
import it.polimi.tiw.tiw2022chioda.bean.Option;
import it.polimi.tiw.tiw2022chioda.bean.Product;
import it.polimi.tiw.tiw2022chioda.bean.User;
import it.polimi.tiw.tiw2022chioda.dao.AvailabilityDAO;
import it.polimi.tiw.tiw2022chioda.dao.EstimateDAO;
import it.polimi.tiw.tiw2022chioda.dao.OptionDAO;
import it.polimi.tiw.tiw2022chioda.dao.ProductDAO;
import it.polimi.tiw.tiw2022chioda.utils.ConnectionHandler;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "GoToClientHome", value = "/GoToClientHome")
public class GoToClientHome extends GoToHome {

    private static final String clientHomePagePath = "WEB-INF/clientHomePage.html";

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
        AvailabilityDAO availabilityDAO = new AvailabilityDAO(connection);
        OptionDAO optionDAO = new OptionDAO(connection);
        User user = (User) session.getAttribute("user");

        List<Estimate> userEstimates = getUserEstimates(estimateDAO, user, response);
        List<Product> products = getAllProducts(productDAO, response);
        List<Option> optionsOfProduct = new ArrayList<>();
        String chosenProduct = request.getParameter("productCode");
        String errorCode = request.getParameter("error");
        int productCode = -1;
        Product actualProduct = null;
        if (chosenProduct != null) {
            productCode = Integer.parseInt(chosenProduct);
            try {
                actualProduct = productDAO.getByCode(productCode);
                List<Integer> optionCodes = availabilityDAO.getFromProduct(productCode);
                for (Integer optionCode : optionCodes) {
                    optionsOfProduct.add(optionDAO.getFromCode(optionCode));
                }
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while recovering options");
                return;
            }
        }
        int error = -1;
        if (errorCode != null) {
            error = Integer.parseInt(errorCode);
            try {
                List<Integer> optionCodes = availabilityDAO.getFromProduct(productCode);
                for (Integer optionCode : optionCodes) {
                    optionsOfProduct.add(optionDAO.getFromCode(optionCode));
                }
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while recovering options");
                return;
            }
        }

        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("estimates", userEstimates);
        ctx.setVariable("products", products);
        ctx.setVariable("actualProductCode", productCode);
        ctx.setVariable("actualProduct", actualProduct);
        ctx.setVariable("error", error);
        ctx.setVariable("options", optionsOfProduct);
        ctx.setVariable("user", user.getUsername());
        templateEngine.process(clientHomePagePath, ctx, response.getWriter());
    }

    protected List<Estimate> getUserEstimates(EstimateDAO estimateDAO, User user, HttpServletResponse response)
            throws IOException {
        List<Estimate> userEstimates;
        try {
            userEstimates = estimateDAO.getByUser(user);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while recovering estimates");
            return new ArrayList<>();
        }
        System.out.println(userEstimates);
        return userEstimates;
    }

    protected List<Product> getAllProducts(ProductDAO productDAO, HttpServletResponse response)
            throws IOException {
        List<Product> products;
        try {
            products = productDAO.getAll();
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while recovering products");
            return new ArrayList<>();
        }
        return products;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
