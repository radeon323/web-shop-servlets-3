package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.Product;
import com.olshevchenko.webshop.service.ProductService;
import com.olshevchenko.webshop.web.servlets.servletutils.ErrorResponseWriter;
import com.olshevchenko.webshop.web.servlets.servletutils.StringToDoubleParser;
import com.olshevchenko.webshop.web.utils.PageGenerator;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
public class AddProductServlet extends HttpServlet {
    private static final String pageFileName = "add_product.html";
    private final ProductService productService = ServiceLocator.get(ProductService.class);
    private final PageGenerator pageGenerator = ServiceLocator.get(PageGenerator.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String page = pageGenerator.getPage(pageFileName);
        response.getWriter().println(page);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Optional<Product> optionalProduct = validateAndGetProduct(request, response);
        optionalProduct.ifPresent(product -> addProduct(product, response));
    }

    Optional<Product> validateAndGetProduct(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        double price = StringToDoubleParser.parseStringToDouble(request.getParameter("price"));
        String description = request.getParameter("description");
        if (!name.isEmpty() && price != 0) {
            Product product = Product.builder()
                    .name(name)
                    .description(description)
                    .price(price)
                    .build();
            return Optional.of(product);
        } else {
            ErrorResponseWriter.writeErrorResponse(response, pageFileName, new HashMap<>());
            return Optional.empty();
        }
    }

    @SneakyThrows
    void addProduct(Product product, HttpServletResponse response) {
        productService.add(product);
        String msgSuccess = String.format("Product <i>%s</i> was successfully added!", product.getName());
        Map<String, Object> parameters = Map.of("msgSuccess", msgSuccess);
        String page = pageGenerator.getPage(pageFileName, parameters);
        response.getWriter().write(page);
    }


}
