package com.olshevchenko.webshop.service;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.Session;
import com.olshevchenko.webshop.entity.User;
import com.olshevchenko.webshop.exception.PasswordIncorrectException;
import com.olshevchenko.webshop.exception.UserNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@RequiredArgsConstructor
public class SecurityService {
    private final UserService userService = ServiceLocator.get(UserService.class);
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private final PasswordEncoder passwordEncoder = new PasswordEncoder();
    private final int cookieTtlMinutes;

    public String providePasswordHashAndSalt(String password) {
        String hashedPassword = passwordEncoder.hashPassword(password);
        String salt = passwordEncoder.getSalt(password);
        return salt + hashedPassword;
    }

    public boolean isTokenValid(String token) {
        if (sessions.containsKey(token)) {
            Session session = sessions.get(token);
            LocalDateTime expireDateTime = session.getExpireDateTime();
            return expireDateTime.isAfter(LocalDateTime.now());
        }
        return false;
    }

    public String generateTokenAndStartNewSession(User user) {
        String token = String.valueOf(UUID.randomUUID());
        sessions.put(token, new Session(token, LocalDateTime.now().plusMinutes(cookieTtlMinutes), user));
        return token;
    }

    public void removeSession(String token) {
        sessions.entrySet().removeIf(e -> e.getKey().equals(token));
    }

    public boolean checkPassword(User user, String password) {
        String saltedHashedPassword = providePasswordHashAndSalt(password);
        if (!saltedHashedPassword.equals(user.getPassword())) {
            throw new PasswordIncorrectException("Incorrect password!");
        }
        return true;
    }

    public User getUser(String email) {
        return userService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Could not find user by email: " + email));
    }


}
