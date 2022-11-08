package com.olshevchenko.webshop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@AllArgsConstructor
public enum Gender {
    MALE("MALE"), FEMALE("FEMALE");
    private final String name;
}
