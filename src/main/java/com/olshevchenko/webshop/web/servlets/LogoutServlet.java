package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.context.InitContext;
import com.olshevchenko.webshop.service.security.SecurityService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Oleksandr Shevchenko
 */
public class LogoutServlet extends HttpServlet {
    private final SecurityService securityService = InitContext.getContext().getBean(SecurityService.class);

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
