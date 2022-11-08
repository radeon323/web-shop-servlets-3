package com.olshevchenko.webshop.web.controller;

import com.olshevchenko.webshop.entity.Gender;
import com.olshevchenko.webshop.entity.User;
import com.olshevchenko.webshop.exception.PasswordIncorrectException;
import com.olshevchenko.webshop.exception.UserNotFoundException;
import com.olshevchenko.webshop.service.UserService;
import com.olshevchenko.webshop.service.security.SecurityService;
import com.olshevchenko.webshop.service.security.entity.Credentials;
import com.olshevchenko.webshop.service.security.entity.Role;
import com.olshevchenko.webshop.service.security.entity.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping()
public class UsersController {

    private final UserService userService;
    private final SecurityService securityService;

    @Value("${cookie.ttl.minutes}")
    private int cookieTtlMinutes;

    @GetMapping("/login")
    protected String getLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    protected String login(@RequestParam String email,
                @RequestParam String password,
                HttpServletResponse response,
                ModelMap model) {
        try {
            Credentials credentials = new Credentials(email, password);
            Session session = securityService.login(credentials);
            Cookie cookie = new Cookie("user-token", session.getToken());
            cookie.setMaxAge(cookieTtlMinutes*60);
            response.addCookie(cookie);
            return "redirect:/products";
        } catch (UserNotFoundException e) {
            String errorMsg = "User not found. Please enter correct email or <a href='/register'>register</a>.";
            model.addAttribute("errorMsg", errorMsg);
            return "login";
        } catch (PasswordIncorrectException e) {
            String errorMsg = "Please enter correct password. <a href='/login'> Forgot password?</a>";
            model.addAttribute("errorMsg", errorMsg);
            return "login";
        }
    }

    @GetMapping("/logout")
    protected String logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("user-token")) {
                securityService.logout(cookie.getValue());
            }
        }
        return "redirect:/products";
    }

    @GetMapping("/register")
    protected String getRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    protected String register(@RequestParam String email, @RequestParam String password,
                              @RequestParam(defaultValue = "MALE") String gender,
                              @RequestParam(defaultValue = "") String firstName,
                              @RequestParam(defaultValue = "") String lastName,
                              @RequestParam(defaultValue = "") String about,
                              @RequestParam(defaultValue = "0") int age,
                              ModelMap model) {

        if (email.isEmpty() || password.isEmpty()) {
            String errorMsg = "Please fill up all necessary fields!";
            model.addAttribute("errorMsg", errorMsg);
            return "register";
        }

        if (userService.findByEmail(email).isPresent()) {
            String errorMsg = "This user is already exist! <a href='/login'> Login page</a>";
            model.addAttribute("errorMsg", errorMsg);
            return "register";
        } else {
            Optional<User> optionalUser = Optional.ofNullable(User.builder().
                    email(email)
                    .password(securityService.providePasswordHashAndSalt(password))
                    .gender(Gender.valueOf(gender.toUpperCase()))
                    .firstName(firstName)
                    .lastName(lastName)
                    .about(about)
                    .age(age)
                    .role(Role.USER)
                    .build());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                userService.add(user);
                String msgSuccess = String.format("User <i>%s</i> was successfully registered!", email);
                model.addAttribute("msgSuccess", msgSuccess);
            }
        }
        return "register";
    }

    @GetMapping("/users/edit")
    protected String getEditUserPage(HttpServletRequest request, ModelMap model) {
        Session session = (Session) request.getAttribute("session");
        model.addAttribute("session", session);
        return "edit_user";
    }

    @PostMapping("/users/edit")
    protected String editUser(@RequestParam String email, @RequestParam String password,
                    @RequestParam(defaultValue = "") String firstName,
                    @RequestParam(defaultValue = "") String lastName,
                    @RequestParam(defaultValue = "") String about,
                    @RequestParam(defaultValue = "0") int age,
                    HttpServletRequest request,
                    ModelMap model) {

        Session session = (Session) request.getAttribute("session");
        User oldUser = session.getUser();

        if (email.isEmpty() || password.isEmpty()) {
            String errorMsg = "Please fill up all necessary fields!";
            model.addAttribute("errorMsg", errorMsg);
            return "edit_user";
        }

        int id = oldUser.getId();
        Role role = oldUser.getRole();
        Gender gender = oldUser.getGender();

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
            model.addAttribute("session", session);

            if (Optional.ofNullable(user).isPresent()) {
                userService.edit(user);
                String msgSuccess = String.format("User <i>%s</i> was successfully changed!", user.getEmail());
                model.addAttribute("msgSuccess", msgSuccess);
            }
        }
        return "edit_user";
    }


}
