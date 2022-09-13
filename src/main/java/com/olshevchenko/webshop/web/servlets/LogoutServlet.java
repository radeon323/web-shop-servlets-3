package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.Session;
import com.olshevchenko.webshop.service.SecurityService;
import com.olshevchenko.webshop.web.servlets.servletutils.SessionFetcher;

import javax.servlet.http.*;
import java.io.IOException;

/**
 * @author Oleksandr Shevchenko
 */
public class LogoutServlet extends HttpServlet {
    private final SecurityService securityService = ServiceLocator.get(SecurityService.class);
    private final SessionFetcher sessionFetcher = new SessionFetcher();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Session session = sessionFetcher.getSession(request);
        if (session != null) {
            String userToken = session.getToken();
            Cookie cookie = new Cookie("user-token", null);
            cookie.setMaxAge(0);
            securityService.removeSession(userToken);
            response.addCookie(cookie);
        }
        response.sendRedirect("/products");
    }

}
