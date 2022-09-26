package com.olshevchenko.webshop.entity;

import com.olshevchenko.webshop.service.security.entity.Role;
import lombok.*;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class User {
    private int id;
    private String email;
    private String password;
    private Gender gender;
    private String firstName;
    private String lastName;
    private String about;
    private int age;
    private Role role;
}

