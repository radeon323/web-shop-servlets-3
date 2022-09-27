package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.Product;
import com.olshevchenko.webshop.service.security.entity.Session;
import com.olshevchenko.webshop.service.ProductService;
import com.olshevchenko.webshop.web.servlets.servletutils.ResponseWriter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public class DeleteProductServlet extends HttpServlet {
    private static final String pageFileName = "products_list.html";
    private final ProductService productService = ServiceLocator.get(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> parameters = new HashMap<>();
        List<Product> products = productService.findAll();

        parameters.put("products", products);

        Session session = (Session) request.getAttribute("session");
        parameters.put("session", session);

        int id = Integer.parseInt(request.getParameter("id"));
        productService.remove(id);

        ResponseWriter.productDeletedResponse(response, pageFileName, parameters, id);
    }
}
