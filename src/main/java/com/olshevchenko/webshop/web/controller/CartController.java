package com.olshevchenko.webshop.web.controller;

import com.olshevchenko.webshop.entity.CartItem;
import com.olshevchenko.webshop.entity.Product;
import com.olshevchenko.webshop.service.CartService;
import com.olshevchenko.webshop.service.ProductService;
import com.olshevchenko.webshop.service.security.entity.Session;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping()
public class CartController {

    private CartService cartService;
    private ProductService productService;

    @GetMapping("/cart")
    protected String getCart(HttpServletRequest request, ModelMap model) {
        createCartAndAddSessionAndReturnListOfCartItems(request, model);
        return "cart";
    }

    @PostMapping("/products")
    protected String addProductToCart(@RequestParam int id, HttpServletRequest request, ModelMap model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);

        Session session = (Session) request.getAttribute("session");

        List<CartItem> cartItems = session.getCart();
        cartService.addToCart(cartItems,id);
        session.setCart(cartItems);

        return "products_list";
    }

    @GetMapping("/cart/delete")
    protected String removeProductFromCart(@RequestParam int id, HttpServletRequest request, ModelMap model) {
        List<CartItem> cartItems = createCartAndAddSessionAndReturnListOfCartItems(request, model);
        cartService.removeFromCart(cartItems, id);
        createCartAndAddSessionAndReturnListOfCartItems(request, model);
        return "cart";
    }

    @PostMapping("/cart/update")
    protected String updateCart(@RequestParam int id, @RequestParam(defaultValue = "0") int quantity, HttpServletRequest request, ModelMap model) {
        List<CartItem> cartItems = createCartAndAddSessionAndReturnListOfCartItems(request, model);
        if (quantity == 0) {
            String errorMsg = "Please specify quantity!";
            model.addAttribute("errorMsg", errorMsg);
            return "cart";
        }
        cartService.updateQuantity(cartItems,id,quantity);
        createCartAndAddSessionAndReturnListOfCartItems(request, model);
        return "cart";
    }

    List<CartItem> createCartAndAddSessionAndReturnListOfCartItems(HttpServletRequest request, ModelMap model) {
        Session session = (Session) request.getAttribute("session");
        model.addAttribute("session", session);
        List<CartItem> cartItems = session.getCart();
        model.addAttribute("cartItems", cartItems);
        double totalPrice = cartService.calculateTotalPrice(cartItems);
        model.addAttribute("totalPrice", totalPrice);
        return cartItems;
    }


}
