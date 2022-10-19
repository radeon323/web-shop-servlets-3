package com.olshevchenko.webshop.service.security;

import com.olshevchenko.webshop.service.security.entity.Role;
import com.olshevchenko.webshop.service.security.entity.Session;
import com.olshevchenko.webshop.entity.User;
import com.olshevchenko.webshop.exception.PasswordIncorrectException;
import com.olshevchenko.webshop.exception.UserNotFoundException;
import com.olshevchenko.webshop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@Service
@PropertySource("classpath:/application.properties")
public class SecurityService {
    private static final List<Session> sessions = Collections.synchronizedList(new ArrayList<>());
    private final PasswordEncoder passwordEncoder = new PasswordEncoder();

    @Autowired
    private UserService userService;

    @Value("${cookie.ttl.minutes}")
    private String cookieTtlMinutes;

    public Session login(String email, String password) {
        User user = getUser(email);
        checkPassword(user, password);
        String token = String.valueOf(UUID.randomUUID());
        Session session = new Session(token, LocalDateTime.now().plusMinutes(Long.parseLong(cookieTtlMinutes)), user);
        sessions.add(session);
        return session;
    }

    public void logout(String userToken) {
        sessions.removeIf(session -> Objects.equals(session.getToken(), userToken));
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
        return sessions.stream()
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
        Optional<Session> optionalSession = sessions.stream()
                .filter(s -> Objects.equals(s.getToken(), userToken))
                .findFirst();
        if (optionalSession.isPresent()) {
            Session session = optionalSession.get();
            boolean isAdmin = session.getUser().getRole().equals(Role.ADMIN);
            boolean isUser = session.getUser().getRole().equals(Role.USER);
            boolean isTokenValid = session.getExpireDateTime().isAfter(LocalDateTime.now());
            return (isAdmin || isUser) && isTokenValid;
        }
        return false;
    }


}
