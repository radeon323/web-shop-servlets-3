package com.olshevchenko.webshop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Session {
    private String token;
    private LocalDateTime expireDateTime;
    private User user;
    private List<CartItem> cart;

    public Session(String token, LocalDateTime localDateTime, User user) {
        this.token = token;
        this.expireDateTime = localDateTime;
        this.user = user;
        this.cart = new ArrayList<>();
    }
}
