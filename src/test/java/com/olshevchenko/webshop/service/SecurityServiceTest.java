package com.olshevchenko.webshop.service;

import com.olshevchenko.webshop.entity.Gender;
import com.olshevchenko.webshop.entity.Role;
import com.olshevchenko.webshop.entity.Session;
import com.olshevchenko.webshop.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Oleksandr Shevchenko
 */
@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {
    private final int cookieTtlMinutes = 300;
    private final String password = "12345";
    private final String token = "7a942e58-ebdb-4cd4-ba8c-a2c594c3fb24";
    private User user;
    private final Session session = new Session(token, LocalDateTime.of(2022, 2,24, 4, 0, 0).plusMinutes(cookieTtlMinutes), user);

    @Mock
    private Map<String, Session> sessions = new ConcurrentHashMap<>();

    @InjectMocks
    private SecurityService securityService = new SecurityService(cookieTtlMinutes);

    @BeforeEach
    void init() {
        user = User.builder()
                .id(1)
                .email("darthvader@gmail.com")
                .password(" �vX���i�G��\u0006\u0005JKJ:�\u0012Y��k]�GB��1�Y�G\u001A�\u0001\u0011*���Y��t��\u0011��\u0006�Y�����s���")
                .gender(Gender.MALE)
                .firstName("Darth")
                .lastName("Vader")
                .about("May the force be with you!")
                .age(50)
                .role(Role.USER)
                .build();
    }

    @Test
    void testProvidePasswordHashAndSalt() {
        String expectedHashedAndSaltedPassword = user.getPassword();
        String actualHashedAndSaltedPassword = securityService.providePasswordHashAndSalt(password);
        assertEquals(expectedHashedAndSaltedPassword,actualHashedAndSaltedPassword);
    }

    //TODO mock the field Map!!!
    @Test
    void testIsTokenValid() {
        when(sessions.containsKey(token)).thenReturn(true);
        when(sessions.get(token)).thenReturn(session);

        assertTrue(securityService.isTokenValid(token));
    }

    @Test
    void testGenerateTokenAndStartNewSession() {
        String generatedToken = securityService.generateTokenAndStartNewSession(user);
        assertEquals(token.length(),generatedToken.length());
    }

    //TODO mock the field Map!!!
    @Test
    void testRemoveSession() {
        sessions.entrySet().removeIf(e -> e.getKey().equals(token));
        when(sessions.containsKey(token)).thenReturn(false);
        assertFalse(sessions.containsKey(token));
    }

    @Test
    void testCheckPassword() {
        assertTrue(securityService.checkPassword(user, password));
    }


}