package com.olshevchenko.webshop.dao.jdbc;

import com.olshevchenko.jdbctemplate.JdbcTemplate;
import com.olshevchenko.webshop.dao.UserDao;
import com.olshevchenko.webshop.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcUserDao implements UserDao {

    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();
    private static final String FIND_BY_ID_SQL = "SELECT id, email, password, gender, firstName, lastName, about, age, role FROM users WHERE id = ?";
    private static final String FIND_BY_EMAIL_SQL = "SELECT id, email, password, gender, firstName, lastName, about, age, role FROM users WHERE email = ?";
    private static final String ADD_SQL = "INSERT INTO users (email, password, gender, firstName, lastName, about, age, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM users WHERE id = ?;";
    private static final String UPDATE_BY_ID_SQL = "UPDATE users SET email = ?, password = ?, gender = ?, firstName = ?, lastName = ?, about = ?, age = ?, role = ? WHERE id = ?;";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> findById(int id) {
        return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, USER_ROW_MAPPER, id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jdbcTemplate.queryForObject(FIND_BY_EMAIL_SQL, USER_ROW_MAPPER, email);
    }

    @Override
    public void add(User user) {
        jdbcTemplate.update(ADD_SQL, user.getEmail(), user.getPassword(), user.getGender().getName(),
                user.getFirstName(), user.getLastName(), user.getAbout(), user.getAge(), user.getRole().getName());
    }

    @Override
    public void remove(int id) {
        jdbcTemplate.update(DELETE_BY_ID_SQL, id);
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(UPDATE_BY_ID_SQL, user.getEmail(), user.getPassword(), user.getGender().getName(),
                user.getFirstName(), user.getLastName(), user.getAbout(), user.getAge(),
                user.getRole().getName(), user.getId());
    }


}
