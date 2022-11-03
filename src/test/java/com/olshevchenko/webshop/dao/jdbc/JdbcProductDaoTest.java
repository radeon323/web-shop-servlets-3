package com.olshevchenko.webshop.dao.jdbc;

import com.olshevchenko.webshop.entity.Product;
import org.apache.commons.dbcp2.BasicDataSource;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Oleksandr Shevchenko
 */
class JdbcProductDaoTest {

    private static final BasicDataSource dataSource = new BasicDataSource();

    private static Product productSamsung;
    private static Product productXiaomi;
    private static Product productApple;
    private static Product productNokia;

    private final JdbcProductDao jdbcProductDao = new JdbcProductDao(dataSource);

    @BeforeAll
    static void init() throws SQLException {
        productSamsung = Product.builder()
                .id(1)
                .name("Samsung Galaxy M52")
                .description("6.7 inches, Qualcomm SM7325 Snapdragon 778G 5G")
                .price(13499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        productXiaomi = Product.builder()
                .id(2)
                .name("Xiaomi Redmi Note 9 Pro")
                .description("6.67 inches, Qualcomm SM7125 Snapdragon 720G Octa-core")
                .price(11699.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        productApple = Product.builder()
                .id(3)
                .name("Apple iPhone 14")
                .description("6.1 inches, Apple A15 Bionic")
                .price(41499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        productNokia = Product.builder()
                .id(4)
                .name("Nokia G11")
                .description("6.5 inches, Unisoc T606")
                .price(4499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();

        dataSource.setUrl("jdbc:h2:mem:test");
        Connection connection = dataSource.getConnection();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(
                        JdbcProductDaoTest.class.getClassLoader().getResourceAsStream("products.sql"))));
        RunScript.execute(connection, bufferedReader);
    }

    @Test
    void testFindAllReturnList() {
        List<Product> expectedProducts = List.of(productSamsung, productXiaomi, productApple);
        List<Product> actualProducts = jdbcProductDao.findAll();
        assertEquals(expectedProducts, actualProducts);
    }

    @Test
    void testFindByIdReturnProduct() {
        Product actualProduct = jdbcProductDao.findById(1);
        assertEquals(productSamsung, actualProduct);
    }

    @Test
    void testAddAndUpdateAndRemove() {
        List<Product> actualProductsBefore = jdbcProductDao.findAll();
        assertFalse(actualProductsBefore.contains(productNokia));
        jdbcProductDao.add(productNokia);
        List<Product> actualProductsAfter = jdbcProductDao.findAll();
        assertTrue(actualProductsAfter.contains(productNokia));

        assertNotEquals("Updated!!!", productNokia.getName());
        productNokia.setName("Updated!!!");
        jdbcProductDao.update(productNokia);
        assertEquals("Updated!!!", productNokia.getName());

        jdbcProductDao.remove(4);
        assertFalse(actualProductsAfter.contains(productNokia));
    }


}