package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.context.InitContext;
import com.olshevchenko.webshop.entity.CartItem;
import com.olshevchenko.webshop.service.security.entity.Session;
import com.olshevchenko.webshop.service.CartService;
import com.olshevchenko.webshop.utils.PageGenerator;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public class ProductCartServlet extends HttpServlet {
    private static final String pageFileName = "cart.html";
    private final PageGenerator pageGenerator = InitContext.getContext().getBean(PageGenerator.class);
    private final CartService cartService = InitContext.getContext().getBean(CartService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HashMap<String, Object> parameters = new HashMap<>();

        Session session = (Session) request.getAttribute("session");
        parameters.put("session", session);

        List<CartItem> cartItems = session.getCart();
        parameters.put("cartItems", cartItems);

        double totalPrice = cartService.calculateTotalPrice(cartItems);
        parameters.put("totalPrice", totalPrice);

        String page = pageGenerator.getPage(pageFileName, parameters);
        response.getWriter().write(page);
    }


}
