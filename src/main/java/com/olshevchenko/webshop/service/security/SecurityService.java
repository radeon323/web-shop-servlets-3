package com.olshevchenko.webshop.service.security;

import com.olshevchenko.webshop.service.security.entity.Credentials;
import com.olshevchenko.webshop.service.security.entity.Session;
import com.olshevchenko.webshop.entity.User;
import com.olshevchenko.webshop.exception.PasswordIncorrectException;
import com.olshevchenko.webshop.exception.UserNotFoundException;
import com.olshevchenko.webshop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@Service
public class SecurityService {
    private final List<Session> sessionList = new CopyOnWriteArrayList<>();
    private final PasswordEncoder passwordEncoder = new PasswordEncoder();

    private final UserService userService;

    @Value("${cookie.ttl.minutes}")
    private int cookieTtlMinutes;

    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public Session login(Credentials credentials) {
        User user = getUser(credentials.getEmail());
        checkPassword(user, credentials.getPassword());
        String token = String.valueOf(UUID.randomUUID());
        Session session = new Session(token, LocalDateTime.now().plusMinutes(cookieTtlMinutes), user);
        sessionList.add(session);
        return session;
    }

    public void logout(String userToken) {
        sessionList.removeIf(session -> Objects.equals(session.getToken(), userToken));
    }

    public String providePasswordHashAndSalt(String password) {
        String hashedPassword = passwordEncoder.hashPassword(password);
        String salt = passwordEncoder.getSalt(password);
        return salt + hashedPassword;
    }

    public Optional<Session> getSession(String userToken) {
        if (userToken == null || !isTokenValid(userToken)) {
            return Optional.empty();
        }
        return sessionList.stream()
                .filter(session -> Objects.equals(session.getToken(), userToken))
                .findFirst();
    }

    private void checkPassword(User user, String password) {
        String saltedHashedPassword = providePasswordHashAndSalt(password);
        if (!saltedHashedPassword.equals(user.getPassword())) {
            throw new PasswordIncorrectException("Incorrect password!");
        }
    }

    private User getUser(String email) {
        return userService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Could not find user by email: " + email));
    }

    private boolean isTokenValid(String userToken) {
        Optional<Session> optionalSession = sessionList.stream()
                .filter(s -> Objects.equals(s.getToken(), userToken))
                .findFirst();
        if (optionalSession.isPresent()) {
            Session session = optionalSession.get();
            return session.getExpireDateTime().isAfter(LocalDateTime.now());
        }
        return false;
    }


}
