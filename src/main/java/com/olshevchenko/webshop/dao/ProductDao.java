package com.olshevchenko.webshop.dao;

import com.olshevchenko.webshop.entity.Product;

import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public interface ProductDao {
    List<Product> findAll();
    Product findById(int id);
    void add(Product product);
    void remove(int id);
    void update(Product product);
}