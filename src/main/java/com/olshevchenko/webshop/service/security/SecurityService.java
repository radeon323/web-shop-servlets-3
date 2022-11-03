package com.olshevchenko.webshop.service.security;

import com.olshevchenko.webshop.service.security.entity.Role;
import com.olshevchenko.webshop.service.security.entity.Session;
import com.olshevchenko.webshop.entity.User;
import com.olshevchenko.webshop.exception.PasswordIncorrectException;
import com.olshevchenko.webshop.exception.UserNotFoundException;
import com.olshevchenko.webshop.service.UserService;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@Setter
@NoArgsConstructor
public class SecurityService {
    private final List<Session> sessionList = Collections.synchronizedList(new ArrayList<>());
    private final PasswordEncoder passwordEncoder = new PasswordEncoder();

    private UserService userService;
    private int cookieTtlMinutes;
    private String excludeUrls;

    public SecurityService(UserService userService, int cookieTtlMinutes) {
        this.userService = userService;
        this.cookieTtlMinutes = cookieTtlMinutes;
    }

    public Session login(String email, String password) {
        User user = getUser(email);
        checkPassword(user, password);
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
            Role role = session.getUser().getRole();
            boolean isTokenValid = session.getExpireDateTime().isAfter(LocalDateTime.now());
            return (role == Role.ADMIN || role == Role.USER) && isTokenValid;
        }
        return false;
    }

    public String getExcludeUrls() {
        return excludeUrls;
    }


    public int getCookieTtlMinutes() {
        return cookieTtlMinutes;
    }
}
