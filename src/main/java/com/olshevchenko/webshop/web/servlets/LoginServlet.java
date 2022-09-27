package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.service.security.entity.Session;
import com.olshevchenko.webshop.exception.PasswordIncorrectException;
import com.olshevchenko.webshop.exception.UserNotFoundException;
import com.olshevchenko.webshop.service.security.SecurityService;
import com.olshevchenko.webshop.web.servlets.servletutils.ResponseWriter;
import com.olshevchenko.webshop.utils.PageGenerator;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;

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
            Session session = securityService.login(email, password);
            Cookie cookie = new Cookie("user-token", session.getToken());
            int cookieTtlMinutes = (int) (Math.round(((Duration.between(LocalDateTime.now(), session.getExpireDateTime()).getSeconds()) + 9.0) / 10 * 10) / 60);
            cookie.setMaxAge(cookieTtlMinutes);
            response.addCookie(cookie);
            response.sendRedirect("/products");
        } catch (UserNotFoundException e) {
            ResponseWriter.writeUserNotExistErrorResponse(response, pageFileName, Collections.emptyMap());
        } catch (PasswordIncorrectException e) {
            ResponseWriter.passwordNotCorrectErrorResponse(response, pageFileName, Collections.emptyMap());
        }
    }


}
