package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.Gender;
import com.olshevchenko.webshop.service.security.entity.Role;
import com.olshevchenko.webshop.entity.User;
import com.olshevchenko.webshop.exception.FieldsNotFilledException;
import com.olshevchenko.webshop.service.security.SecurityService;
import com.olshevchenko.webshop.service.UserService;
import com.olshevchenko.webshop.web.servlets.servletutils.RequestExtractor;
import com.olshevchenko.webshop.web.servlets.servletutils.ResponseWriter;
import com.olshevchenko.webshop.web.servlets.servletutils.StringParser;
import com.olshevchenko.webshop.utils.PageGenerator;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Oleksandr Shevchenko
 */
public class RegisterServlet extends HttpServlet {
    private static final String pageFileName = "register.html";
    private final UserService userService = ServiceLocator.get(UserService.class);
    private final SecurityService securityService = ServiceLocator.get(SecurityService.class);
    private final PageGenerator pageGenerator = ServiceLocator.get(PageGenerator.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String page = pageGenerator.getPage(pageFileName);
        response.getWriter().println(page);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Optional<User> optionalUser = validateAndGetUser(request, response);
        optionalUser.ifPresent(user -> addUser(user, response));
    }

    Optional<User> validateAndGetUser(HttpServletRequest request, HttpServletResponse response) {
        String email;
        String password;
        try {
            email = RequestExtractor.getStringFromRequest(request, "email");
            password = RequestExtractor.getStringFromRequest(request, "password");
        } catch (FieldsNotFilledException e) {
            ResponseWriter.writeFieldsErrorResponse(response, pageFileName, Collections.emptyMap());
            return Optional.empty();
        }

        if (userService.findByEmail(email).isPresent()) {
            ResponseWriter.writeUserExistErrorResponse(response, pageFileName, Collections.emptyMap());
            return Optional.empty();
        } else {
            Gender gender = Gender.valueOf(Optional.of(request.getParameter("gender").toUpperCase()).filter(Predicate.not(String::isEmpty)).orElse("MALE"));
            String firstName = Optional.ofNullable(request.getParameter("firstName")).filter(Predicate.not(String::isEmpty)).orElse("");
            String lastName = Optional.ofNullable(request.getParameter("lastName")).filter(Predicate.not(String::isEmpty)).orElse("");
            String about = Optional.ofNullable(request.getParameter("about")).filter(Predicate.not(String::isEmpty)).orElse("");
            int age = StringParser.parseStringToInteger(request.getParameter("age"));

            User user = User.builder().
                    email(email)
                    .password(securityService.providePasswordHashAndSalt(password))
                    .gender(gender)
                    .firstName(firstName)
                    .lastName(lastName)
                    .about(about)
                    .age(age)
                    .role(Role.USER)
                    .build();
            return Optional.of(user);
        }
    }

    void addUser(User user, HttpServletResponse response) {
        userService.add(user);
        ResponseWriter.writeUserRegisteredResponse(response, pageFileName, Collections.emptyMap(), user.getEmail());
    }


}
