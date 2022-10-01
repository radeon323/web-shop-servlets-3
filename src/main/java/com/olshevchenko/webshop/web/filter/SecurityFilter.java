package com.olshevchenko.webshop.web.filter;

import com.olshevchenko.webshop.context.Context;
import com.olshevchenko.webshop.service.security.entity.Session;
import com.olshevchenko.webshop.service.security.SecurityService;
import com.olshevchenko.webshop.utils.PropertiesReader;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@Setter
@ToString
@EqualsAndHashCode
public class SecurityFilter implements Filter {
    private final SecurityService securityService = Context.getContext().getBean(SecurityService.class);
    private final List<String> allowedPaths;

    public SecurityFilter() {
        PropertiesReader propertiesReader = Context.getContext().getBean(PropertiesReader.class);
        this.allowedPaths = propertiesReader.getAllowedUriPaths();
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


}
