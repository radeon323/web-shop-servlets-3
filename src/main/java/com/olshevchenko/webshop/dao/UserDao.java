package com.olshevchenko.webshop.dao;

import com.olshevchenko.webshop.entity.User;

import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
public interface UserDao {
    Optional<User> findById(int id);
    Optional<User> findByEmail (String email);
    void add(User user);
    void remove(int id);
    void update(User user);
}
