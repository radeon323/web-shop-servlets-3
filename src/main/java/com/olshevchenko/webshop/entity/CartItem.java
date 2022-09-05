package com.olshevchenko.webshop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Oleksandr Shevchenko
 */
@Data
@AllArgsConstructor
public class CartItem {
    private Product product;
    private int quantity;
}
