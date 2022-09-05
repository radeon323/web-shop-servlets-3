package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.Gender;
import com.olshevchenko.webshop.entity.Role;
import com.olshevchenko.webshop.entity.User;
import com.olshevchenko.webshop.service.SecurityService;
import com.olshevchenko.webshop.service.UserService;
import com.olshevchenko.webshop.web.servlets.servletutils.ResponseWriter;
import com.olshevchenko.webshop.web.servlets.servletutils.StringParser;
import com.olshevchenko.webshop.web.utils.PageGenerator;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
public class RegisterServlet extends HttpServlet {
    private static final String pageFileName = "register.html";
    private final UserService userService = ServiceLocator.get(UserService.class);
    private final SecurityService securityService = ServiceLocator.get(SecurityService.class);
    private final PageGenerator pageGenerator = ServiceLocator.get(PageGenerator.class);

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String page = pageGenerator.getPage(pageFileName);
        response.getWriter().println(page);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Optional<User> optionalUser = validateAndGetUser(request, response);
        optionalUser.ifPresent(user -> addUser(user, response));
    }

    Optional<User> validateAndGetUser(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        boolean isUserExist = userService.findByEmail(email) != null;

        if (isUserExist) {
            ResponseWriter.writeUserExistErrorResponse(response, pageFileName, new HashMap<>());
        }

        if (!isUserExist && !email.isEmpty() && !password.isEmpty()) {
            Gender gender = Optional.of(Gender.valueOf(request.getParameter("gender").toUpperCase())).orElse(Gender.MALE);
            String firstName = Optional.ofNullable(request.getParameter("firstName")).orElse("");
            String lastName = Optional.ofNullable(request.getParameter("lastName")).orElse("");
            String about = Optional.ofNullable(request.getParameter("about")).orElse("");
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
        } else {
            ResponseWriter.writeFieldsErrorResponse(response, pageFileName, new HashMap<>());
            return Optional.empty();
        }
    }

    @SneakyThrows
    void addUser(User user, HttpServletResponse response) {
        userService.add(user);
        String msgSuccess = String.format("User <i>%s</i> was successfully registered!", user.getEmail());
        Map<String, Object> parameters = Map.of("msgSuccess", msgSuccess);
        String page = pageGenerator.getPage(pageFileName, parameters);
        response.getWriter().write(page);
    }


}
