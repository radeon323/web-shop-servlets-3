package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.Product;
import com.olshevchenko.webshop.service.ProductService;
import com.olshevchenko.webshop.web.servlets.servletutils.ResponseWriter;
import com.olshevchenko.webshop.web.servlets.servletutils.SessionFetcher;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public class DeleteProductServlet extends HttpServlet {
    private static final String pageFileName = "products_list.html";
    private final ProductService productService = ServiceLocator.get(ProductService.class);
    private final SessionFetcher sessionFetcher = new SessionFetcher();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> parameters = new HashMap<>();
        List<Product> products = productService.findAll();

        parameters.put("products", products);

        sessionFetcher.validateAndPutSessionToPageParameters(request, parameters);

        int id = Integer.parseInt(request.getParameter("id"));
        productService.remove(id);

        ResponseWriter.productDeletedResponse(response, pageFileName, parameters, id);
    }
}
