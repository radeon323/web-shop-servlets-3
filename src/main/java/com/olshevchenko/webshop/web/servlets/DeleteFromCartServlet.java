package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.CartItem;
import com.olshevchenko.webshop.entity.Session;
import com.olshevchenko.webshop.service.CartService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public class DeleteFromCartServlet extends HttpServlet {
    private final CartService cartService = ServiceLocator.get(CartService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Session session = (Session) request.getAttribute("session");

        List<CartItem> cartItems = session.getCart();

        int id = Integer.parseInt(request.getParameter("id"));
        cartService.removeFromCart(cartItems, id);

        response.sendRedirect("/cart");
    }
}
