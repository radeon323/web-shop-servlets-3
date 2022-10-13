package com.olshevchenko.webshop.web.controller;

import com.olshevchenko.webshop.entity.CartItem;
import com.olshevchenko.webshop.entity.Product;
import com.olshevchenko.webshop.service.CartService;
import com.olshevchenko.webshop.service.ProductService;
import com.olshevchenko.webshop.service.security.entity.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping()
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    @GetMapping("/cart")
    String getCart(HttpServletRequest request, ModelMap model) {
        Session session = (Session) request.getAttribute("session");
        model.addAttribute("session", session);

        List<CartItem> cartItems = session.getCart();
        model.addAttribute("cartItems", cartItems);

        double totalPrice = cartService.calculateTotalPrice(cartItems);
        model.addAttribute("totalPrice", totalPrice);
        return "cart";
    }

    @PostMapping("/products")
    String addProductToCart(@RequestParam int id, HttpServletRequest request, ModelMap model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);

        Session session = (Session) request.getAttribute("session");

        List<CartItem> cartItems = session.getCart();
        cartService.addToCart(cartItems,id);
        session.setCart(cartItems);

        return "products_list";
    }

    @GetMapping("/cart/delete")
    String removeProductFromCart(@RequestParam int id, HttpServletRequest request) {
        Session session = (Session) request.getAttribute("session");

        List<CartItem> cartItems = session.getCart();
        cartService.removeFromCart(cartItems, id);

        return "cart";
    }

    @PostMapping("/cart/update")
    String updateCart(@RequestParam int id, HttpServletRequest request) {
        Session session = (Session) request.getAttribute("session");

        List<CartItem> cartItems = session.getCart();
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        cartService.updateQuantity(cartItems,id,quantity);
        session.setCart(cartItems);

        return "cart";
    }


}
