package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.Product;
import com.olshevchenko.webshop.service.ProductService;
import com.olshevchenko.webshop.web.servlets.servletutils.ResponseWriter;
import com.olshevchenko.webshop.web.servlets.servletutils.SessionFetcher;
import com.olshevchenko.webshop.web.servlets.servletutils.StringParser;
import com.olshevchenko.webshop.web.utils.PageGenerator;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private final SessionFetcher sessionFetcher = new SessionFetcher();

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = productService.findById(id);
        oldParameters.put("product", product);

        sessionFetcher.validateAndPutSessionToPageParameters(request, oldParameters);

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
        double price = StringParser.parseStringToDouble(request.getParameter("price"));
        String description = request.getParameter("description");
        if (!name.isEmpty() && price != 0) {
            Product product = Product.builder()
                    .id(id)
                    .name(name)
                    .description(description)
                    .price(price)
                    .build();
            parameters.put("product", product);
            sessionFetcher.validateAndPutSessionToPageParameters(request, parameters);
            return Optional.of(product);
        } else {
            ResponseWriter.writeFieldsErrorResponse(response, pageFileName, oldParameters);
            return Optional.empty();
        }
    }

    @SneakyThrows
    void editProduct(Product product, HttpServletResponse response, Map<String, Object> parameters) {
        productService.edit(product);
        ResponseWriter.productEditedResponse(response, pageFileName, parameters, product.getName());
    }


}
