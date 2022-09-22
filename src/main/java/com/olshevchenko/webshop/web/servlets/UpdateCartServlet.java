package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.CartItem;
import com.olshevchenko.webshop.entity.Session;
import com.olshevchenko.webshop.service.CartService;
import com.olshevchenko.webshop.web.servlets.servletutils.SessionFetcher;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public class UpdateCartServlet extends HttpServlet {
    private final CartService cartService = ServiceLocator.get(CartService.class);
    private final SessionFetcher sessionFetcher = new SessionFetcher();


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HashMap<String, Object> parameters = new HashMap<>();

        sessionFetcher.validateAndPutSessionToPageParameters(request, parameters);
        Session session = sessionFetcher.getSession(request);

        List<CartItem> cartItems = session.getCart();
        int id = Integer.parseInt(request.getParameter("id"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        cartService.updateQuantity(cartItems,id,quantity);
        session.setCart(cartItems);
        parameters.put("cartItems", cartItems);

        response.sendRedirect("/cart");
    }
}
