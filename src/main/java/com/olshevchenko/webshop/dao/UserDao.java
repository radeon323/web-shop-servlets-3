package com.olshevchenko.webshop.dao;

import com.olshevchenko.webshop.entity.User;

/**
 * @author Oleksandr Shevchenko
 */
public interface UserDao {
    User findById(int id);
    User findByEmail (String email);
    void add(User user);
    void remove(int id);
    void update(User user);
}
