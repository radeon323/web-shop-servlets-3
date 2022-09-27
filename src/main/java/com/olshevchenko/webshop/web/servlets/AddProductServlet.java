package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.Product;
import com.olshevchenko.webshop.service.security.entity.Session;
import com.olshevchenko.webshop.service.ProductService;
import com.olshevchenko.webshop.web.servlets.servletutils.ResponseWriter;
import com.olshevchenko.webshop.web.servlets.servletutils.StringParser;
import com.olshevchenko.webshop.utils.PageGenerator;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
public class AddProductServlet extends HttpServlet {
    private static final Map<String, Object> parameters = new HashMap<>();
    private static final String pageFileName = "add_product.html";
    private final ProductService productService = ServiceLocator.get(ProductService.class);
    private final PageGenerator pageGenerator = ServiceLocator.get(PageGenerator.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        parameters.remove("msgSuccess");
        Session session = (Session) request.getAttribute("session");
        parameters.put("session", session);
        String page = pageGenerator.getPage(pageFileName, parameters);
        response.getWriter().println(page);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Optional<Product> optionalProduct = validateAndGetProduct(request, response);
        optionalProduct.ifPresent(product -> addProduct(product, response));
    }

    Optional<Product> validateAndGetProduct(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        double price = StringParser.parseStringToDouble(request.getParameter("price"));
        String description = request.getParameter("description");
        if (!name.isEmpty() && price != 0) {
            Product product = Product.builder()
                    .name(name)
                    .description(description)
                    .price(price)
                    .build();
            return Optional.of(product);
        } else {
            ResponseWriter.writeFieldsErrorResponse(response, pageFileName, parameters);
            return Optional.empty();
        }
    }

    void addProduct(Product product, HttpServletResponse response) {
        productService.add(product);
        ResponseWriter.productAddedResponse(response, pageFileName, parameters, product.getName());
    }


}
