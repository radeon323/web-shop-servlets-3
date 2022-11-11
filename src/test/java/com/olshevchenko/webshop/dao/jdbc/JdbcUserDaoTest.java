package com.olshevchenko.webshop.dao.jdbc;

import com.olshevchenko.webshop.entity.Gender;
import com.olshevchenko.webshop.entity.User;
import com.olshevchenko.webshop.service.security.entity.Role;
import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Oleksandr Shevchenko
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcUserDaoTest {
    private final BasicDataSource dataSource = new BasicDataSource();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    private final JdbcUserDao jdbcUserDao = new JdbcUserDao(jdbcTemplate);
    private final User darthVader;
    private final User lukeSkywalker;

    JdbcUserDaoTest() throws SQLException {
        darthVader = User.builder()
                .id(1)
                .email("darthvader@gmail.com")
                .password(" �vX���i�G��\u0006\u0005JKJ:�\u0012Y��k]�GB��1�Y�G\u001A�\u0001\u0011*���Y��t��\u0011��\u0006�Y�����s���")
                .gender(Gender.MALE)
                .firstName("Darth")
                .lastName("Vader")
                .about("I'm your father")
                .age(41)
                .role(Role.USER)
                .build();

        lukeSkywalker = User.builder()
                .id(2)
                .email("lukeskywalker@gmail.com")
                .password(" �vX���i�G��\u0006\u0005JKJ:�\u0012Y��k]�GB��1�Y�G\u001A�\u0001\u0011*���Y��t��\u0011��\u0006�Y�����s���")
                .gender(Gender.MALE)
                .firstName("Luke")
                .lastName("Skywalker")
                .about("May the force be with you!!!")
                .age(19)
                .role(Role.ADMIN)
                .build();

        dataSource.setUrl("jdbc:h2:mem:test");
        Connection connection = dataSource.getConnection();

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(
                        JdbcUserDaoTest.class.getClassLoader().getResourceAsStream("users.sql"))));
        RunScript.execute(connection, bufferedReader);
    }

    @Test
    void testFindByIdReturnUser() {
        Optional<User> actualUser = jdbcUserDao.findById(1);
        assertTrue(actualUser.isPresent());
        assertEquals(darthVader, actualUser.get());
    }

    @Test
    void testFindByEmailReturnUser() {
        Optional<User> actualUser = jdbcUserDao.findByEmail("darthvader@gmail.com");
        assertTrue(actualUser.isPresent());
        assertEquals(darthVader, actualUser.get());
    }

    @Test
    void testAddAndUpdateAndRemove() {
        jdbcUserDao.add(lukeSkywalker);
        Optional<User> actualUser2 = jdbcUserDao.findById(2);
        assertTrue(actualUser2.isPresent());
        assertEquals(lukeSkywalker, actualUser2.get());

        lukeSkywalker.setEmail("skywalker@gmail.com");
        jdbcUserDao.update(lukeSkywalker);
        Optional<User> actualUser = jdbcUserDao.findByEmail("skywalker@gmail.com");
        assertTrue(actualUser.isPresent());
        assertEquals(lukeSkywalker, actualUser.get());

        jdbcUserDao.remove(2);
    }

}