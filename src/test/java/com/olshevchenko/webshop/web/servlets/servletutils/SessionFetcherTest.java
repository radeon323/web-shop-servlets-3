package com.olshevchenko.webshop.web.servlets.servletutils;

import com.olshevchenko.webshop.entity.Gender;
import com.olshevchenko.webshop.entity.Role;
import com.olshevchenko.webshop.entity.Session;
import com.olshevchenko.webshop.entity.User;
import com.olshevchenko.webshop.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Oleksandr Shevchenko
 */
@ExtendWith(MockitoExtension.class)
class SessionFetcherTest {
    private final int cookieTtlMinutes = 300;
    private final String token = "7a942e58-ebdb-4cd4-ba8c-a2c594c3fb24";
    private User user;
    private final Session session = new Session(token, LocalDateTime.of(2022, 2,24, 4, 0, 0).plusMinutes(cookieTtlMinutes), user);
    private final SessionFetcher sessionFetcher = new SessionFetcher();

    @Mock
    private SecurityService securityService;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private Map<String, Session> sessions;

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

    //TODO
    @Test
    void testGetSession() {
        Cookie cookie = new Cookie("user-token", token);
        Cookie[] cookies = {cookie};
        when(requestMock.getCookies()).thenReturn(cookies);

        when(securityService.isTokenValid(token)).thenReturn(true);

        when(securityService.getSessions()).thenReturn(sessions);

        when(sessions.get(token)).thenReturn(session);

        Session actualSession = sessionFetcher.getSession(requestMock);
        assertEquals(session, actualSession);
    }

    @Test
    void testGetUserToken() {
        Cookie cookie = new Cookie("user-token", token);
        Cookie[] cookies = {cookie};
        when(requestMock.getCookies()).thenReturn(cookies);
        String actualToken = sessionFetcher.getUserToken(requestMock);
        assertEquals(token, actualToken);
    }
}