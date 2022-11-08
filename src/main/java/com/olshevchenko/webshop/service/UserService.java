package com.olshevchenko.webshop.service;

import com.olshevchenko.webshop.dao.UserDao;
import com.olshevchenko.webshop.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserDao jdbcUserDao;

    public Optional<User> findById(int id) {
        return jdbcUserDao.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return jdbcUserDao.findByEmail(email);
    }

    public void add(User user) {
        jdbcUserDao.add(user);
    }

    public void remove(int id) {
        jdbcUserDao.remove(id);
    }

    public void edit(User user) {
        jdbcUserDao.update(user);
    }
}
