package com.olshevchenko.webshop.web.filter;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.Role;
import com.olshevchenko.webshop.entity.Session;
import com.olshevchenko.webshop.service.SecurityService;
import com.olshevchenko.webshop.web.servlets.servletutils.SessionFetcher;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
public class SecurityFilter implements Filter {
    private final SecurityService securityService = ServiceLocator.get(SecurityService.class);
    private final SessionFetcher sessionFetcher = new SessionFetcher();
    private final List<String> allowedPaths;

    //TODO Question: If I transfer allowedPaths from properties through constructor, I get java.lang.NoSuchMethodException
    public SecurityFilter() {
        this.allowedPaths = List.of("/login", "/login/", "/logout", "/logout/", "/register", "/register/");
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)  request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)  response;

        String requestURI = httpServletRequest.getRequestURI();
        for (String allowedPath : allowedPaths) {
            if (requestURI.startsWith(allowedPath) || requestURI.equals("/products") || requestURI.equals("/products/")) {
                chain.doFilter(request, response);
                return;
            }
        }

        Session session = sessionFetcher.getSession(httpServletRequest);

        if (isAuth(session)) {
            httpServletRequest.setAttribute("session", session);
            log.info("Authorized!");
            chain.doFilter(request, response);
        } else {
            httpServletResponse.sendRedirect("/login");
        }

    }

    private boolean isAuth(Session session) {
        if (session == null) {
            return false;
        }
        boolean isAdmin = session.getUser().getRole().equals(Role.ADMIN);
        boolean isUser = session.getUser().getRole().equals(Role.USER);
        boolean isTokenValid = securityService.isTokenValid(session.getToken());
        return (isAdmin || isUser) && isTokenValid;
    }

    @Override
    public void destroy() {
    }
}
