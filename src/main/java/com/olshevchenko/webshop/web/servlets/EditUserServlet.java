package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.Gender;
import com.olshevchenko.webshop.service.security.entity.Role;
import com.olshevchenko.webshop.service.security.entity.Session;
import com.olshevchenko.webshop.entity.User;
import com.olshevchenko.webshop.exception.FieldsNotFilledException;
import com.olshevchenko.webshop.exception.UserNotFoundException;
import com.olshevchenko.webshop.service.security.SecurityService;
import com.olshevchenko.webshop.service.UserService;
import com.olshevchenko.webshop.web.servlets.servletutils.RequestExtractor;
import com.olshevchenko.webshop.web.servlets.servletutils.ResponseWriter;
import com.olshevchenko.webshop.web.servlets.servletutils.StringParser;
import com.olshevchenko.webshop.utils.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Oleksandr Shevchenko
 */
public class EditUserServlet extends HttpServlet {
    private static final Map<String, Object> oldParameters = new HashMap<>();
    private static final String pageFileName = "edit_user.html";
    private final SecurityService securityService = ServiceLocator.get(SecurityService.class);
    private final UserService userService = ServiceLocator.get(UserService.class);
    private final PageGenerator pageGenerator = ServiceLocator.get(PageGenerator.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Session session = (Session) request.getAttribute("session");
            oldParameters.put("session", session);
            String page = pageGenerator.getPage(pageFileName, oldParameters);
            response.getWriter().write(page);
        } catch (UserNotFoundException e) {
            ResponseWriter.writeUserNotExistErrorResponse(response, pageFileName, Collections.emptyMap());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> newParameters = new HashMap<>();
        Optional<User> optionalUser = validateAndGetUser(request, response, newParameters, oldParameters);
        optionalUser.ifPresent(user -> editUser(user, response, newParameters));
    }

    Optional<User> validateAndGetUser(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameters, Map<String, Object> oldParameters) {
        Session session = (Session) request.getAttribute("session");
        User oldUser = session.getUser();

        String email;
        String password;
        try {
            email = RequestExtractor.getStringFromRequest(request, "email");
            password = RequestExtractor.getStringFromRequest(request, "password");
        } catch (FieldsNotFilledException e) {
            ResponseWriter.writeFieldsErrorResponse(response, pageFileName, oldParameters);
            return Optional.empty();
        }

        int id = oldUser.getId();
        Gender gender = Gender.valueOf(Optional.of(request.getParameter("gender").toUpperCase()).filter(Predicate.not(String::isEmpty)).orElse(oldUser.getGender().toString()));
        String firstName = Optional.ofNullable(request.getParameter("firstName")).filter(Predicate.not(String::isEmpty)).orElse(oldUser.getFirstName());
        String lastName = Optional.ofNullable(request.getParameter("lastName")).filter(Predicate.not(String::isEmpty)).orElse(oldUser.getLastName());
        String about = Optional.ofNullable(request.getParameter("about")).filter(Predicate.not(String::isEmpty)).orElse(oldUser.getAbout());
        int age = StringParser.parseStringToInteger(request.getParameter("age"));
        Role role = oldUser.getRole();

        if (userService.findByEmail(email).isPresent()) {
            User user = User.builder()
                    .id(id)
                    .email(email)
                    .password(securityService.providePasswordHashAndSalt(password))
                    .gender(gender)
                    .firstName(firstName)
                    .lastName(lastName)
                    .about(about)
                    .age(age)
                    .role(role)
                    .build();
            session.setUser(user);

            parameters.put("session", session);

            return Optional.of(user);
        } else {
            ResponseWriter.writeFieldsErrorResponse(response, pageFileName, oldParameters);
            return Optional.empty();
        }
    }

    void editUser(User user, HttpServletResponse response, Map<String, Object> parameters) {
        userService.edit(user);
        ResponseWriter.userEditedResponse(response, pageFileName, parameters, user.getEmail());
    }
}
