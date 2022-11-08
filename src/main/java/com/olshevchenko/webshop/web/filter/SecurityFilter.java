package com.olshevchenko.webshop.web.filter;

import com.olshevchenko.webshop.service.security.entity.Session;
import com.olshevchenko.webshop.service.security.SecurityService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

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
    private SecurityService securityService;
    private List<String> urlExcludeList;

    @Override
    public void init(FilterConfig filterConfig) {
        ServletContext servletContext = filterConfig.getServletContext();
        WebApplicationContext webApplicationContext =
                WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        securityService = webApplicationContext.getBean(SecurityService.class);
        urlExcludeList = webApplicationContext.getBean(ExcludedUrls.class).getUrlExcludeList();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)  request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)  response;

        String requestURI = httpServletRequest.getRequestURI();
        for (String url : urlExcludeList) {
            if (requestURI.startsWith(url)) {
                chain.doFilter(request, response);
                return;
            }
        }

        String userToken = getUserToken(httpServletRequest);
        Optional<Session> optionalSession = securityService.getSession(userToken);
        if (optionalSession.isPresent()) {
            Session session = optionalSession.get();
            httpServletRequest.setAttribute("session", session);
            log.info("Authorized as user: {}", session.getUser().getEmail());
            chain.doFilter(request, response);
        } else if (requestURI.equals("/products") || requestURI.equals("/products/")) {
            log.info("Unauthorized...");
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
