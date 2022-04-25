package it.polimi.tiw.tiw2022chioda.utils;

import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.lang.Class.forName;

public class ConnectionHandler {

    public static Connection getConnection(ServletContext context)
            throws UnavailableException {
        Connection connection = null;
        try {
            String driver = context.getInitParameter("dbDriver");
            String url = context.getInitParameter("dbUrl");
            String user = context.getInitParameter("dbUser");
            String password = context.getInitParameter("dbPassword");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new UnavailableException("Couldn't get db connection");
        } catch (ClassNotFoundException e) {
            throw new UnavailableException("Can't load database driver");
        }
        return connection;
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if(connection != null){
            connection.close();
        }
    }
}
