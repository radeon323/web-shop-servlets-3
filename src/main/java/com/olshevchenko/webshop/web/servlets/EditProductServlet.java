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
public class EditProductServlet extends HttpServlet {
    private static final Map<String, Object> oldParameters = new HashMap<>();
    private static final String pageFileName = "edit_product.html";
    private final ProductService productService = ServiceLocator.get(ProductService.class);
    private final PageGenerator pageGenerator = ServiceLocator.get(PageGenerator.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = productService.findById(id);
        oldParameters.put("product", product);

        String page = pageGenerator.getPage(pageFileName, oldParameters);
        response.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> newParameters = new HashMap<>();
        Optional<Product> optionalProduct = validateAndGetProduct(request, response, newParameters, oldParameters);
        optionalProduct.ifPresent(product -> editProduct(product, response, newParameters));
    }

    Optional<Product> validateAndGetProduct(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameters, Map<String, Object> oldParameters) {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        double price = StringToDoubleParser.parseStringToDouble(request.getParameter("price"));
        String description = request.getParameter("description");
        if (!name.isEmpty() && price != 0) {
            Product product = Product.builder()
                    .id(id)
                    .name(name)
                    .description(description)
                    .price(price)
                    .build();
            parameters.put("product", product);
            return Optional.of(product);
        } else {
            ErrorResponseWriter.writeErrorResponse(response, pageFileName, oldParameters);
            return Optional.empty();
        }
    }

    @SneakyThrows
    void editProduct(Product product, HttpServletResponse response, Map<String, Object> parameters) {
        productService.edit(product);
        String msgSuccess = String.format("Product <i>%s</i> was successfully changed!", product.getName());
        parameters.put("msgSuccess", msgSuccess);
        String page = pageGenerator.getPage(pageFileName, parameters);
        response.getWriter().write(page);
    }


}
