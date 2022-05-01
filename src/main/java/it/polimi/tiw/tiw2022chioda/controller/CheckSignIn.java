package it.polimi.tiw.tiw2022chioda.controller;

import it.polimi.tiw.tiw2022chioda.bean.User;
import it.polimi.tiw.tiw2022chioda.dao.UserDAO;
import it.polimi.tiw.tiw2022chioda.utils.ConnectionHandler;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;

@WebServlet(name = "CheckSignIn", value = "/CheckSignIn")
public class CheckSignIn extends HttpServlet {

    private Connection connection;

    public void init() throws ServletException {
        System.out.println("CheckSignIn initialization");
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("CheckLogin got Get");
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("CheckLogin got Post");
        String username = null;
        String password = null;
        String repeatedPassword = null;
        String userType = null;
        String eMail = null;

        username = StringEscapeUtils.escapeJava(request.getParameter("username"));
        password = StringEscapeUtils.escapeJava(request.getParameter("password"));
        repeatedPassword = StringEscapeUtils.escapeJava(request.getParameter("rep-password"));
        userType = StringEscapeUtils.escapeJava(request.getParameter("usertype"));
        eMail = StringEscapeUtils.escapeJava(request.getParameter("email"));

        //Controls
        if (username == null ||
                password == null ||
                repeatedPassword == null ||
                userType == null ||
                eMail == null ||
                username.isEmpty() ||
                password.isEmpty() ||
                repeatedPassword.isEmpty() ||
                userType.isEmpty() ||
                eMail.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Credentials must be not empty");
            return;
        }
        if(!password.equals(repeatedPassword)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Passwords do not coincide");
            return;
        }
        if(!isEMail(eMail)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Given e-mail is not a valid e-mail address");
            return;
        }
        UserDAO userDAO = new UserDAO(connection);
        try {
            if (userDAO.isUsernamePresent(username)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Username already used");
                return;
            }
        } catch(SQLException e){
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Error while querying database: " + e.getMessage());
            return;
        }
        User user = new User();
        try {
            user.setUserType(userType);
            user.setUsername(username);
            user.setEmail(eMail);
            user = userDAO.registerCredentials(user, password);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Error while querying database: " + e.getMessage());
            return;
        }
        String path = getServletContext().getContextPath();
        if (user == null) {
            path = path + "/index.html";
        } else {
            request.getSession().setAttribute("user", user);
            path = path + "/GoToHome";
        }
        response.sendRedirect(path);
    }

    private boolean isEMail(String EMail){
        return Pattern.matches("([a-zA-Z0-9.])+@(\\w+\\.)+\\w+", EMail);
    }
}
