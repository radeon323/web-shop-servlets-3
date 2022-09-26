package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.service.security.SecurityService;

import javax.servlet.http.*;
import java.io.IOException;

/**
 * @author Oleksandr Shevchenko
 */
public class LogoutServlet extends HttpServlet {
    private final SecurityService securityService = ServiceLocator.get(SecurityService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("user-token")) {
                securityService.logout(cookie.getValue());
            }
        }
        response.sendRedirect("/login");
    }


}
