package com.olshevchenko.webshop.service.security;

import com.olshevchenko.webshop.service.security.entity.Role;
import com.olshevchenko.webshop.service.security.entity.Session;
import com.olshevchenko.webshop.entity.User;
import com.olshevchenko.webshop.exception.PasswordIncorrectException;
import com.olshevchenko.webshop.exception.UserNotFoundException;
import com.olshevchenko.webshop.service.UserService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class SecurityService {
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private final PasswordEncoder passwordEncoder = new PasswordEncoder();
    private UserService userService;
    private int cookieTtlMinutes;

    public Session login(String email, String password) {
        User user = getUser(email);
        checkPassword(user, password);
        String token = String.valueOf(UUID.randomUUID());
        Session session = new Session(token, LocalDateTime.now().plusMinutes(cookieTtlMinutes), user);
        sessions.put(token, session);
        return session;
    }

    public void logout(String userToken) {
        sessions.remove(userToken);
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
        return Optional.ofNullable(sessions.get(userToken));
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
        if (sessions.containsKey(userToken)) {
            Session session = sessions.get(userToken);
            boolean isAdmin = session.getUser().getRole().equals(Role.ADMIN);
            boolean isUser = session.getUser().getRole().equals(Role.USER);
            boolean isTokenValid = session.getExpireDateTime().isAfter(LocalDateTime.now());
            return (isAdmin || isUser) && isTokenValid;
        }
        return false;
    }


}
