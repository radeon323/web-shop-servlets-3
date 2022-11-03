package com.olshevchenko.webshop.dao.jdbc;

import com.olshevchenko.webshop.dao.ProductDao;
import com.olshevchenko.webshop.entity.Product;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JdbcProductDao implements ProductDao {

    private static final ProductRowMapper PRODUCT_ROW_MAPPER = new ProductRowMapper();
    private static final String FIND_ALL_SQL = "SELECT id, name, description, price, creation_date FROM products";
    private static final String FIND_BY_ID_SQL = "SELECT id, name, description, price, creation_date FROM products WHERE id = ?";
    private static final String ADD_SQL = "INSERT INTO products (name, description, price, creation_date) VALUES (?, ?, ?, ?)";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM products WHERE id = ?";
    private static final String UPDATE_BY_ID_SQL = "UPDATE products SET name = ?, description = ?, price = ? WHERE id = ?";
    private DataSource dataSource;

    @Override
    public List<Product> findAll() {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL);
            ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Product> products = Collections.synchronizedList(new ArrayList<>());
            while(resultSet.next()) {
                products.add(PRODUCT_ROW_MAPPER.mapRow(resultSet));
            }
            return products;
        } catch (SQLException e) {
            log.error("Cannot execute query: {} ", FIND_ALL_SQL, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product findById(int id) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return PRODUCT_ROW_MAPPER.mapRow(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            log.error("Cannot execute query: {} ", FIND_BY_ID_SQL, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Product product) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_SQL)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(product.getCreationDate()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Cannot execute query: {} ", ADD_SQL, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(int id) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Cannot execute query: {} ", DELETE_BY_ID_SQL, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Product product) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BY_ID_SQL)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setInt(4, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Cannot execute query: {} ", UPDATE_BY_ID_SQL, e);
            throw new RuntimeException(e);
        }
    }


}
