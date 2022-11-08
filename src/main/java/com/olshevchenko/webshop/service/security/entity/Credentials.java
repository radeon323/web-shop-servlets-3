package com.olshevchenko.webshop.service.security.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@AllArgsConstructor
public class Credentials {
    private String email;
    private String password;
}
