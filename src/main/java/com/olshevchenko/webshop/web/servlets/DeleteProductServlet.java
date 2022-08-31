package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.Product;
import com.olshevchenko.webshop.service.ProductService;
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
public class DeleteProductServlet extends HttpServlet {
    private final ProductService productService = ServiceLocator.get(ProductService.class);
    private final PageGenerator pageGenerator = ServiceLocator.get(PageGenerator.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HashMap<String, Object> parameters = new HashMap<>();
        List<Product> products = productService.findAll();

        parameters.put("products", products);

        int id = Integer.parseInt(request.getParameter("id"));
        productService.remove(id);
        String msgSuccess = String.format("Product with id:%d was successfully deleted!", id);
        parameters.put("msgSuccess", msgSuccess);
        String page = pageGenerator.getPage("products_list.html", parameters);
        response.getWriter().write(page);
    }
}
