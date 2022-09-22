package com.olshevchenko.webshop.web.servlets;

import javax.servlet.http.*;
import java.io.IOException;

/**
 * @author Oleksandr Shevchenko
 */
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("user-token")) {
                response.addCookie(new Cookie("user-token", null));
                response.sendRedirect("/login");
            }
        }
    }


}
