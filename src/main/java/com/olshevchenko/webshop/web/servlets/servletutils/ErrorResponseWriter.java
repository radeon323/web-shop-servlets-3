package com.olshevchenko.webshop.web.servlets.servletutils;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.web.utils.PageGenerator;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Oleksandr Shevchenko
 */
public class ErrorResponseWriter {
    private static final PageGenerator pageGenerator = ServiceLocator.get(PageGenerator.class);

    @SneakyThrows
    public static void writeErrorResponse(HttpServletResponse response, String filename, Map<String, Object> parameters) {
        String errorMsg = "Please fill up all necessary fields!";
        parameters.put("errorMsg", errorMsg);
        String pageWithError = pageGenerator.getPage(filename, parameters);
        response.getWriter().write(pageWithError);
    }
}
