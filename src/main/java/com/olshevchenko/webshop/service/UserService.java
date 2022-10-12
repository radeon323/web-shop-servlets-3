package com.olshevchenko.webshop.service;

import com.olshevchenko.webshop.dao.UserDao;
import com.olshevchenko.webshop.entity.User;
import com.olshevchenko.webshop.exception.UserNotFoundException;
import lombok.*;

import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserService {
    private UserDao userDao;

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
