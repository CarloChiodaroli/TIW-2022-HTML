package it.polimi.tiw.tiw2022chioda.filters;

import it.polimi.tiw.tiw2022chioda.bean.User;
import it.polimi.tiw.tiw2022chioda.enums.UserType;
import it.polimi.tiw.tiw2022chioda.utils.ErrorSender;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ClientChecker implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String loginPath = httpServletRequest.getServletContext().getContextPath() + "/index.html";
        HttpSession session = httpServletRequest.getSession();
        User user;
        user = (User) session.getAttribute("user");
        if (!user.getUserType().equals(UserType.CLIENT)) {
            ErrorSender.user(httpServletResponse, HttpServletResponse.SC_FORBIDDEN, "Logged user is not " + UserType.CLIENT);
            return;
        }
        chain.doFilter(request, response);
    }
}
