package com.olshevchenko.webshop.dao;

import com.olshevchenko.webshop.entity.Product;

import java.util.List;
import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
public interface ProductDao {
    List<Product> findAll();
    Optional<Product> findById(int id);
    void add(Product product);
    void remove(int id);
    void update(Product product);
}