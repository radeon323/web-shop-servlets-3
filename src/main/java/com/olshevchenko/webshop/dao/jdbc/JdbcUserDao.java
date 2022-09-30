package com.olshevchenko.webshop.dao.jdbc;

import com.olshevchenko.webshop.dao.UserDao;
import com.olshevchenko.webshop.entity.User;
import com.olshevchenko.webshop.utils.DataSourceFactory;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@Setter
@ToString
@EqualsAndHashCode
public class JdbcUserDao implements UserDao {

    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();
    private static final String FIND_BY_ID_SQL = "SELECT id, email, password, gender, firstName, lastName, about, age, role FROM users WHERE id = ?";
    private static final String FIND_BY_EMAIL_SQL = "SELECT id, email, password, gender, firstName, lastName, about, age, role FROM users WHERE email = ?";
    private static final String ADD_SQL = "INSERT INTO users (email, password, gender, firstName, lastName, about, age, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM users WHERE id = ?;";
    private static final String UPDATE_BY_ID_SQL = "UPDATE users SET email = ?, password = ?, gender = ?, firstName = ?, lastName = ?, about = ?, age = ?, role = ? WHERE id = ?;";
    private DataSourceFactory dataSourceFactory;

    @Override
    public Optional<User> findById(int id) {
        DataSource dataSource = dataSourceFactory.createDataSource();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return Optional.of(USER_ROW_MAPPER.mapRow(resultSet));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            log.error("Cannot execute query: {} ", FIND_BY_ID_SQL, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        DataSource dataSource = dataSourceFactory.createDataSource();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_EMAIL_SQL)) {
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return Optional.of(USER_ROW_MAPPER.mapRow(resultSet));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            log.error("Cannot execute query: {} ", FIND_BY_EMAIL_SQL, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(User user) {
        DataSource dataSource = dataSourceFactory.createDataSource();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_SQL);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setObject(3, user.getGender().toString(), Types.OTHER);
            preparedStatement.setString(4, user.getFirstName());
            preparedStatement.setString(5, user.getLastName());
            preparedStatement.setString(6, user.getAbout());
            preparedStatement.setInt(7, user.getAge());
            preparedStatement.setObject(8, user.getRole().toString(), Types.OTHER);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Cannot execute query: {} ", ADD_SQL, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(int id) {
        DataSource dataSource = dataSourceFactory.createDataSource();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            log.error("Cannot execute query: {} ", DELETE_BY_ID_SQL, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User user) {
        DataSource dataSource = dataSourceFactory.createDataSource();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BY_ID_SQL)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setObject(3, user.getGender().toString(), Types.OTHER);
            preparedStatement.setString(4, user.getFirstName());
            preparedStatement.setString(5, user.getLastName());
            preparedStatement.setString(6, user.getAbout());
            preparedStatement.setInt(7, user.getAge());
            preparedStatement.setObject(8, user.getRole().toString(), Types.OTHER);
            preparedStatement.setInt(9, user.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            log.error("Cannot execute query: {} ", UPDATE_BY_ID_SQL, e);
            throw new RuntimeException(e);
        }
    }


}
