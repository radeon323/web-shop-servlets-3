package com.olshevchenko.webshop.exception;

/**
 * @author Oleksandr Shevchenko
 */
public class PasswordIncorrectException extends RuntimeException {
    public PasswordIncorrectException(String message) {
        super(message);
    }
}
