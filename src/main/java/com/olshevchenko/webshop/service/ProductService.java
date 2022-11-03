package com.olshevchenko.webshop.service;

import com.olshevchenko.webshop.dao.ProductDao;
import com.olshevchenko.webshop.entity.Product;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductService {

    private ProductDao productDao;

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public Product findById(int id) {
        return productDao.findById(id);
    }

    public void add(Product product) {
        product.setCreationDate(LocalDateTime.now().withNano(0).withSecond(0));
        productDao.add(product);
    }

    public void remove(int id) {
        productDao.remove(id);
    }

    public void edit(Product product) {
        productDao.update(product);
    }
}
