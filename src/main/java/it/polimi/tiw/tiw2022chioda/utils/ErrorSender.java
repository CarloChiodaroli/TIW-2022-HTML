package it.polimi.tiw.tiw2022chioda.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorSender {

    public static void server(HttpServletResponse response) throws IOException {
        server(response, "Internal server error - Please retry");
    }

    public static void server(HttpServletResponse response, String message) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error - Please retry");
    }

    public static void database(HttpServletResponse response) throws IOException {
        server(response, "Error While querying database");
    }

    public static void database(HttpServletResponse response, String what) throws IOException {
        server(response, "Error While querying database when " + what);
    }

    public static void user(HttpServletResponse response, int sc, String message) throws IOException {
        response.sendError(sc, message);
    }

    public static void user(HttpServletResponse response, String message) throws IOException {
        user(response, HttpServletResponse.SC_BAD_REQUEST, message);
    }

    public static void userWrongData(HttpServletResponse response, String message) throws IOException {
        user(response, message);
    }

    public static void userNotNumber(HttpServletResponse response) throws IOException {
        user(response, "Got a wrongly formatted number");
    }

    public static void userNotNumber(HttpServletResponse response, String dataName) throws IOException {
        user(response, "Got a wrongly formatted number: " + dataName);
    }
    public static void wrongHttp(HttpServletResponse response, String method) throws IOException {
        user(response, "This servlet does not accept " + method + "s requests");
    }
}
