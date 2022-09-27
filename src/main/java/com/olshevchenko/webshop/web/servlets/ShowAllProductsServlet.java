package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.CartItem;
import com.olshevchenko.webshop.entity.Product;
import com.olshevchenko.webshop.service.security.entity.Session;
import com.olshevchenko.webshop.service.CartService;
import com.olshevchenko.webshop.service.ProductService;
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
public class ShowAllProductsServlet extends HttpServlet {
    private static final String pageFileName = "products_list.html";
    private final ProductService productService = ServiceLocator.get(ProductService.class);
    private final PageGenerator pageGenerator = ServiceLocator.get(PageGenerator.class);
    private final CartService cartService = ServiceLocator.get(CartService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HashMap<String, Object> parameters = new HashMap<>();
        List<Product> products = productService.findAll();
        parameters.put("products", products);

        Session session = (Session) request.getAttribute("session");
        parameters.put("session", session);

        String page = pageGenerator.getPage(pageFileName, parameters);
        response.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Session session = (Session) request.getAttribute("session");

        List<CartItem> cartItems = session.getCart();

        int id = Integer.parseInt(request.getParameter("id"));
        cartService.addToCart(cartItems,id);
        session.setCart(cartItems);
        response.sendRedirect("/products");
    }


}
