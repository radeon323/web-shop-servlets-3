package com.olshevchenko.webshop.web.servlets.servletutils;

import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
public class StringParser {
    public static double parseStringToDouble(String value) {
        Optional<Double> optional = value == null || value.isEmpty() ? Optional.empty() : Optional.of(Double.valueOf(value));
        return optional.isPresent() ? optional.get() : 0;
    }

    public static int parseStringToInteger(String value) {
        Optional<Integer> optional = value == null || value.isEmpty() ? Optional.empty() : Optional.of(Integer.valueOf(value));
        return optional.orElse(0);
    }
}
