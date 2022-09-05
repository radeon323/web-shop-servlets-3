package com.olshevchenko.webshop.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleksandr Shevchenko
 */
class PasswordEncoderTest {

    private final PasswordEncoder passwordEncoder = new PasswordEncoder();

    private final String password = "12345";

    @Test
    void testHashPassword() {
        String expectedHashedPassword = "Y�G\u001A�\u0001\u0011*���Y��t��\u0011��\u0006�Y�����s���";
        String actualHashedPassword = passwordEncoder.hashPassword(password);
        assertEquals(expectedHashedPassword,actualHashedPassword);
    }

    @Test
    void testGetSalt() {
        String expectedSalt = " �vX���i�G��\u0006\u0005JKJ:�\u0012Y��k]�GB��1�";
        String actualSalt = passwordEncoder.getSalt(password);
        assertEquals(expectedSalt,actualSalt);
    }
}