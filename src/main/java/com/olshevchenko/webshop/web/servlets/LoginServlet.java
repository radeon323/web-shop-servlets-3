package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.User;
import com.olshevchenko.webshop.exception.PasswordIncorrectException;
import com.olshevchenko.webshop.exception.UserNotFoundException;
import com.olshevchenko.webshop.service.SecurityService;
import com.olshevchenko.webshop.web.servlets.servletutils.ResponseWriter;
import com.olshevchenko.webshop.web.utils.PageGenerator;

import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Oleksandr Shevchenko
 */
public class LoginServlet extends HttpServlet {
    private static final String pageFileName = "login.html";
    private final SecurityService securityService = ServiceLocator.get(SecurityService.class);
    private final PageGenerator pageGenerator = ServiceLocator.get(PageGenerator.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String page = pageGenerator.getPage(pageFileName);
        response.getWriter().println(page);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = securityService.getUser(email);
            securityService.checkPassword(user, password);

            String userToken = securityService.generateTokenAndStartNewSession(user);
            Cookie cookie = new Cookie("user-token", userToken);
            cookie.setMaxAge(securityService.getCookieTtlMinutes());
            response.addCookie(cookie);
            response.sendRedirect("/products");
        } catch (UserNotFoundException e) {
            ResponseWriter.writeUserNotExistErrorResponse(response, pageFileName, new HashMap<>());
        } catch (PasswordIncorrectException e) {
            ResponseWriter.passwordNotCorrectErrorResponse(response, pageFileName, new HashMap<>());
        }
    }


}
