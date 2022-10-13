package com.olshevchenko.webshop.service;

import com.olshevchenko.webshop.dao.UserDao;
import com.olshevchenko.webshop.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public Optional<User> findById(int id) {
        return userDao.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public void add(User user) {
        userDao.add(user);
    }

    public void remove(int id) {
        userDao.remove(id);
    }

    public void edit(User user) {
        userDao.update(user);
    }
}
