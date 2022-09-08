package com.olshevchenko.webshop.exception;

/**
 * @author Oleksandr Shevchenko
 */
public class FieldsNotFilledException extends RuntimeException {
    public FieldsNotFilledException(String message) {
        super(message);
    }
}
