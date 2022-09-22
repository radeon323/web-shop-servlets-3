package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.CartItem;
import com.olshevchenko.webshop.entity.Session;
import com.olshevchenko.webshop.service.CartService;
import com.olshevchenko.webshop.web.servlets.servletutils.SessionFetcher;
import com.olshevchenko.webshop.web.utils.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public class ProductCartServlet extends HttpServlet {
    private static final String pageFileName = "cart.html";
    private final PageGenerator pageGenerator = ServiceLocator.get(PageGenerator.class);
    private final CartService cartService = ServiceLocator.get(CartService.class);
    private final SessionFetcher sessionFetcher = new SessionFetcher();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HashMap<String, Object> parameters = new HashMap<>();

        sessionFetcher.validateAndPutSessionToPageParameters(request, parameters);
        Session session = sessionFetcher.getSession(request);

        List<CartItem> cartItems = session.getCart();
        parameters.put("cartItems", cartItems);

        double totalPrice = cartService.calculateTotalPrice(cartItems);
        parameters.put("totalPrice", totalPrice);

        String page = pageGenerator.getPage(pageFileName, parameters);
        response.getWriter().write(page);
    }


}
