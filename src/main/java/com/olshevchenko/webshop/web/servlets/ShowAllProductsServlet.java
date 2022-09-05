package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.Product;
import com.olshevchenko.webshop.entity.Session;
import com.olshevchenko.webshop.entity.User;
import com.olshevchenko.webshop.service.ProductService;
import com.olshevchenko.webshop.service.SecurityService;
import com.olshevchenko.webshop.web.servlets.servletutils.SessionFetcher;
import com.olshevchenko.webshop.web.utils.PageGenerator;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public class ShowAllProductsServlet extends HttpServlet {
    private static final String pageFileName = "products_list.html";
    private final ProductService productService = ServiceLocator.get(ProductService.class);
    private final PageGenerator pageGenerator = ServiceLocator.get(PageGenerator.class);
    private final SessionFetcher sessionFetcher = new SessionFetcher();

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> parameters = new HashMap<>();
        List<Product> products = productService.findAll();

        parameters.put("products", products);

        sessionFetcher.validateAndPutSessionToPageParameters(request, parameters);

        String page = pageGenerator.getPage(pageFileName, parameters);
        response.getWriter().write(page);
    }


}
