package com.olshevchenko.webshop.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Oleksandr Shevchenko
 */
public class PasswordEncoder {
    private final MessageDigest MESSAGE_DIGEST;

    public PasswordEncoder() {
        try {
            MESSAGE_DIGEST = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }

    public String hashPassword(String password) {
        MESSAGE_DIGEST.update(password.getBytes());
        byte[] hash = MESSAGE_DIGEST.digest();
        return new String(hash, StandardCharsets.UTF_8);
    }

    public String getSalt(String password) {
        StringBuilder passwordBuilder = new StringBuilder(password);
        passwordBuilder.reverse();
        String salt = passwordBuilder.toString();
        return hashPassword(salt);
    }
}
