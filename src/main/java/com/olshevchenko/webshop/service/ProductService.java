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
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductDao jdbcProductDao;

    public List<Product> findAll() {
        return jdbcProductDao.findAll();
    }

    public Product findById(int id) {
        return jdbcProductDao.findById(id);
    }

    public void add(Product product) {
        product.setCreationDate(LocalDateTime.now().withNano(0).withSecond(0));
        jdbcProductDao.add(product);
    }

    public void remove(int id) {
        jdbcProductDao.remove(id);
    }

    public void edit(Product product) {
        jdbcProductDao.update(product);
    }
}
