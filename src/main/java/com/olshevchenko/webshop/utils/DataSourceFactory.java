package com.olshevchenko.webshop.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@AllArgsConstructor
public class DataSourceFactory {
    private Properties properties;

    public DataSource createDataSource() {
        String jdbcUrl = properties.getProperty("jdbc.url");
        String jdbcUser = properties.getProperty("jdbc.user");
        String jdbcPassword = properties.getProperty("jdbc.password");

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUser);
        dataSource.setPassword(jdbcPassword);
        return dataSource;
    }
}
