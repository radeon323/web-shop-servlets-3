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
    private PropertiesReader propertiesReader;

    public DataSource createDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(propertiesReader.getProperties().getProperty("jdbc.url"));
        dataSource.setUsername(propertiesReader.getProperties().getProperty("jdbc.user"));
        dataSource.setPassword(propertiesReader.getProperties().getProperty("jdbc.password"));
        return dataSource;
    }
}
