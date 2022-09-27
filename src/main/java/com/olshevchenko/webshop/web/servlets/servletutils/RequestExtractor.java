package com.olshevchenko.webshop.web.servlets.servletutils;

import com.olshevchenko.webshop.exception.FieldsNotFilledException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Oleksandr Shevchenko
 */
public class RequestExtractor {

    public static String getStringFromRequest(HttpServletRequest request, String parameter) {
        String result = request.getParameter(parameter);
        if (result.isEmpty()) {
            throw new FieldsNotFilledException("Empty field: " + parameter);
        }
        return result;
    }


}
