package com.olshevchenko.webshop.service.security.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@AllArgsConstructor
public enum Role {
    ADMIN("ADMIN"), USER("USER"), GUEST("GUEST");
    private final String name;
}
