package com.olshevchenko.webshop.service;

import com.olshevchenko.webshop.entity.CartItem;
import com.olshevchenko.webshop.entity.Product;
import lombok.*;

import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartService {

    private ProductService productService;

    public void addToCart(List<CartItem> cart, int id) {
        Product product = productService.findById(id);
        cart.stream()
                .filter(item -> item.getProduct().getId() == id)
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + 1),
                        () -> cart.add(new CartItem(product, 1)));
    }

    public void removeFromCart(List<CartItem> cart, int id) {
        CartItem cartItem = cart.stream()
                .filter(item -> item.getProduct().getId() == id)
                .findFirst()
                .orElse(null);
        cart.remove(cartItem);
    }

    public void updateQuantity(List<CartItem> cart, int id, int quantity) {
        cart.stream()
                .filter(item -> item.getProduct().getId() == id)
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));
    }

    public double calculateTotalPrice(List<CartItem> cart) {
        double total = 0;
        for (CartItem item : cart) {
            total = total + item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }


}
