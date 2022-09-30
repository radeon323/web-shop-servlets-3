package com.olshevchenko.webshop.utils;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class DataSourceFactory {
    private String jdbcUrl;
    private String jdbcUser;
    private String jdbcPassword;

    public DataSource createDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUser);
        dataSource.setPassword(jdbcPassword);
        return dataSource;
    }
}
