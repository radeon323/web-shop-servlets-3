package com.olshevchenko.webshop.web.servlets.servletutils;

import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
public class StringToDoubleParser {
    public static double parseStringToDouble(String value) {
        Optional<Double> optionalPrice = value == null || value.isEmpty() ? Optional.empty() : Optional.of(Double.valueOf(value));
        return optionalPrice.isPresent() ? optionalPrice.get() : 0;
    }
}
