package com.olshevchenko.webshop.dao.jdbc;

import com.olshevchenko.webshop.entity.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author Oleksandr Shevchenko
 */
public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        int id  = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        double price  = resultSet.getDouble("price");
        Timestamp creationDate = resultSet.getTimestamp("creation_date");
        return Product.builder().
                id(id)
                .name(name)
                .description(description)
                .price(price)
                .creationDate(creationDate.toLocalDateTime())
                .build();
    }
}
