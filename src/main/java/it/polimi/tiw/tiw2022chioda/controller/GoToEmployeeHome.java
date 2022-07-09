package it.polimi.tiw.tiw2022chioda.controller;

import it.polimi.tiw.tiw2022chioda.bean.Estimate;
import it.polimi.tiw.tiw2022chioda.bean.Product;
import it.polimi.tiw.tiw2022chioda.bean.User;
import it.polimi.tiw.tiw2022chioda.dao.EstimateDAO;
import it.polimi.tiw.tiw2022chioda.dao.ProductDAO;
import it.polimi.tiw.tiw2022chioda.utils.ConnectionHandler;
import it.polimi.tiw.tiw2022chioda.utils.ErrorSender;
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
import java.util.List;

@WebServlet(name = "GoToEmployeeHome", value = "/GoToEmployeeHome")
public class GoToEmployeeHome extends GoToHome {

    private static final String employeeHomePagePath = "WEB-INF/employeeHomePage.html";

    @Serial
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    private Connection connection = null;


    @Override
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
        User user = (User) session.getAttribute("user");
        List<Estimate> pricedEstimates;
        List<Estimate> notPricedEstimates;
        try {
            pricedEstimates = estimateDAO.getByUser(user);
        } catch (SQLException e) {
            ErrorSender.database(response, "getting estimates by user");
            return;
        }

        try {
            notPricedEstimates = estimateDAO.getNotPriced();
        } catch (SQLException e) {
            ErrorSender.database(response, "getting not priced estimates");
            return;
        }

        List<Product> products = new ArrayList<>();
        try {
            products = productDAO.getAll();
        } catch (SQLException e) {
            ErrorSender.database(response, "getting products");
            return;
        }

        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("pricedEstimates", pricedEstimates);
        ctx.setVariable("products", products);
        ctx.setVariable("notPricedEstimates", notPricedEstimates);
        ctx.setVariable("user", user.getUsername());
        templateEngine.process(employeeHomePagePath, ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ErrorSender.wrongHttp(response, "Post");
    }
}
