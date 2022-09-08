package com.olshevchenko.webshop.service;

import com.olshevchenko.webshop.entity.CartItem;
import com.olshevchenko.webshop.entity.Product;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@AllArgsConstructor
public class CartService {
    private final ProductService productService;

    public void addToCart(List<CartItem> cart, int id) {
        Product product = productService.findById(id);
        cart.stream()
                .filter(item -> item.getProduct().getId() == id)
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + 1),
                        () -> cart.add(new CartItem(product, 1)));
    }


}
