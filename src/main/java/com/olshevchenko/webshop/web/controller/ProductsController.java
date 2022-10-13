package com.olshevchenko.webshop.web.controller;

import com.olshevchenko.webshop.entity.Product;
import com.olshevchenko.webshop.service.ProductService;
import com.olshevchenko.webshop.service.security.entity.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductService productService;

    @GetMapping()
    protected String showAllProducts(HttpServletRequest request, ModelMap model) {
        createProductsListAndAddSession(request, model);
        return "products_list";
    }

    @GetMapping("/add")
    protected String getAddProductPage(HttpServletRequest request, ModelMap model) {
        model.remove("msgSuccess");
        Session session = (Session) request.getAttribute("session");
        model.addAttribute("session", session);
        return "add_product";
    }

    @PostMapping("/add")
    protected String addProduct(@RequestParam(defaultValue = "0") int id,
                      @RequestParam String name,
                      @RequestParam(defaultValue = "0") double price,
                      @RequestParam(defaultValue = "") String description,
                      ModelMap model) {

        Optional<Product> optionalProduct = validateAndGetProduct(id, name, price, description, model);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            model.addAttribute("product", product);
            productService.add(product);
            String msgSuccess = String.format("Product <i>%s</i> was successfully added!", product.getName());
            model.addAttribute("msgSuccess", msgSuccess);
        }
        return "add_product";
    }

    @GetMapping("/edit")
    protected String getEditProductPage(HttpServletRequest request,
                              @RequestParam int id,
                              ModelMap model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);

        Session session = (Session) request.getAttribute("session");
        model.addAttribute("session", session);
        return "edit_product";
    }

    @PostMapping("/edit")
    protected String editProduct(@RequestParam int id,
                       @RequestParam String name,
                       @RequestParam(defaultValue = "0") double price,
                       @RequestParam(defaultValue = "") String description,
                       ModelMap model) {

        Optional<Product> optionalProduct = validateAndGetProduct(id, name, price, description, model);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            model.addAttribute("product", product);
            productService.edit(product);
            String msgSuccess = String.format("Product <i>%s</i> was successfully changed!", product.getName());
            model.addAttribute("msgSuccess", msgSuccess);
        }
        return "edit_product";
    }

    @GetMapping("/delete")
    protected String deleteProduct(@RequestParam int id,
                         HttpServletRequest request,
                         ModelMap model) {
        productService.remove(id);
        String msgSuccess = String.format("Product with id:%d was successfully deleted!", id);
        model.addAttribute("msgSuccess", msgSuccess);

        createProductsListAndAddSession(request, model);
        return "products_list";
    }


    void createProductsListAndAddSession(HttpServletRequest request, ModelMap model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);

        Session session = (Session) request.getAttribute("session");
        model.addAttribute("session", session);
    }


    Optional<Product> validateAndGetProduct(int id, String name, double price, String description, ModelMap model) {
        Product product = Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .description(description)
                .build();
        if (!name.isEmpty() && price != 0) {
            return Optional.of(product);
        } else {
            String errorMsg = "Please fill up all necessary fields!";
            model.addAttribute("errorMsg", errorMsg);
            model.addAttribute("product", product);
            return Optional.empty();
        }
    }








}
