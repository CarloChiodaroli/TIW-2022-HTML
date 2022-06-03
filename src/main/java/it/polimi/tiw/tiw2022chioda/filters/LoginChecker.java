package it.polimi.tiw.tiw2022chioda.filters;

import it.polimi.tiw.tiw2022chioda.enums.UserType;
import it.polimi.tiw.tiw2022chioda.utils.ErrorSender;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginChecker implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String loginPath = httpServletRequest.getServletContext().getContextPath() + "/index.html";
        HttpSession session = httpServletRequest.getSession();
        if (session.isNew() || session.getAttribute("user") == null) {
            ErrorSender.user(httpServletResponse, HttpServletResponse.SC_UNAUTHORIZED, "No user ha logged in");
            return;
        }
        chain.doFilter(request, response);
    }
}
