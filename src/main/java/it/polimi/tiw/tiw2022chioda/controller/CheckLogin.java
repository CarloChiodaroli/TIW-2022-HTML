package it.polimi.tiw.tiw2022chioda.controller;

import it.polimi.tiw.tiw2022chioda.bean.User;
import it.polimi.tiw.tiw2022chioda.dao.UserDAO;
import it.polimi.tiw.tiw2022chioda.utils.ConnectionHandler;
import it.polimi.tiw.tiw2022chioda.utils.ErrorSender;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "CheckLogin", value = "/CheckLogin")
public class CheckLogin extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;
    private Connection connection = null;

    public CheckLogin() {
        super();
    }

    public void init() throws ServletException {
        System.out.println("CheckLogin initialization");
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("CheckLogin got Get");
        ErrorSender.wrongHttp(response, "Get");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("CheckLogin started");

        String username = null;
        String password = null;

        username = StringEscapeUtils.escapeJava(request.getParameter("username"));
        password = StringEscapeUtils.escapeJava(request.getParameter("password"));
        if(username == null ||
                password == null ||
                username.isEmpty() ||
                password.isEmpty()){
            System.out.println("CheckLogin got empty Credentials");
            ErrorSender.userWrongData(response, "Credentials must be not empty");
            return;
        }

        UserDAO userDAO = new UserDAO(connection);
        User user;
        try {
            user = userDAO.checkCredentials(username, password);
        } catch (SQLException e) {
            ErrorSender.database(response, "checking credentials");
            return;
        }

        if(user == null){
            ErrorSender.user(response, "Credentials are not valid");
            return;
        }

        request.getSession().setAttribute("user", user);
        response.sendRedirect(getServletContext().getContextPath() + "/GoToHome");
    }
}
