package it.polimi.tiw.tiw2022chioda.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorSender {

    public static void server(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().println("Internal server error - Please retry");
    }

    public static void user(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println(message);
    }
}
