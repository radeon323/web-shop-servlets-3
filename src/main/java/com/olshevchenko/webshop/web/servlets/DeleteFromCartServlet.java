package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.context.Context;
import com.olshevchenko.webshop.entity.CartItem;
import com.olshevchenko.webshop.service.security.entity.Session;
import com.olshevchenko.webshop.service.CartService;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public class DeleteFromCartServlet extends HttpServlet {
    private final CartService cartService = Context.getContext().getBean(CartService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Session session = (Session) request.getAttribute("session");

        List<CartItem> cartItems = session.getCart();

        int id = Integer.parseInt(request.getParameter("id"));
        cartService.removeFromCart(cartItems, id);

        response.sendRedirect("/cart");
    }
}
