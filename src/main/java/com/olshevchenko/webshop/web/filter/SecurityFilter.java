package com.olshevchenko.webshop.web.filter;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.service.security.entity.Session;
import com.olshevchenko.webshop.service.security.SecurityService;
import com.olshevchenko.webshop.utils.PropertiesReader;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
public class SecurityFilter implements Filter {
    private final SecurityService securityService = ServiceLocator.get(SecurityService.class);
    private final List<String> allowedPaths;

    public SecurityFilter() {
        PropertiesReader propertiesReader = ServiceLocator.get(PropertiesReader.class);
        this.allowedPaths = propertiesReader.getAllowedUriPaths();
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
            if (requestURI.startsWith(allowedPath)) {
                chain.doFilter(request, response);
                return;
            }
        }

        String userToken = getUserToken(httpServletRequest);
        Optional<Session> optionalSession = securityService.getSession(userToken);

        if (optionalSession.isPresent()) {
            httpServletRequest.setAttribute("session", optionalSession.get());
            log.info("Authorized!");
            chain.doFilter(request, response);
        } else if (requestURI.equals("/products") || requestURI.equals("/products/")) {
            chain.doFilter(request, response);
        } else {
            httpServletResponse.sendRedirect("/login");
        }
    }

    private String getUserToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("user-token"))
                    .findFirst()
                    .map(Cookie::getValue).orElse(null);
        }
        return null;
    }


    @Override
    public void destroy() {
    }
}
