package com.olshevchenko.webshop.service;

import com.olshevchenko.webshop.dao.ProductDao;
import com.olshevchenko.webshop.entity.Product;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductDao productDao;

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
