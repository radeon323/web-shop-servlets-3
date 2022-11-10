package com.olshevchenko.webshop.dao.jdbc;

import com.olshevchenko.webshop.dao.ProductDao;
import com.olshevchenko.webshop.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcProductDao implements ProductDao {

    private static final ProductRowMapper PRODUCT_ROW_MAPPER = new ProductRowMapper();
    private static final String FIND_ALL_SQL = "SELECT id, name, description, price, creation_date FROM products";
    private static final String FIND_BY_ID_SQL = "SELECT id, name, description, price, creation_date FROM products WHERE id = ?";
    private static final String ADD_SQL = "INSERT INTO products (name, description, price, creation_date) VALUES (?, ?, ?, ?)";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM products WHERE id = ?";
    private static final String UPDATE_BY_ID_SQL = "UPDATE products SET name = ?, description = ?, price = ? WHERE id = ?";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, PRODUCT_ROW_MAPPER);
    }

    @Override
    public Optional<Product> findById(int id) {
        Product product = jdbcTemplate.queryForObject(FIND_BY_ID_SQL, PRODUCT_ROW_MAPPER, id);
        return Optional.ofNullable(product);
    }

    @Override
    public void add(Product product) {
        jdbcTemplate.update(ADD_SQL, product.getName(), product.getDescription(),
                product.getPrice(), product.getCreationDate());
    }

    @Override
    public void remove(int id) {
        jdbcTemplate.update(DELETE_BY_ID_SQL, id);
    }

    @Override
    public void update(Product product) {
        jdbcTemplate.update(UPDATE_BY_ID_SQL, product.getName(), product.getDescription(),
                product.getPrice(), product.getId());
    }


}
