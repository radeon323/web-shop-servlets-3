package com.olshevchenko.webshop.web.utils;

import org.flywaydb.core.Flyway;

import java.util.Properties;

/**
 * @author Oleksandr Shevchenko
 */
public class DbMigration {

    public static void main(String[] args) {
        Properties properties = new PropertiesReader().getProperties("application.properties");
        DbMigration.migrate(properties);
    }

    public static void migrate(Properties properties) {
        String jdbcUrl = properties.getProperty("jdbc.url");
        String jdbcUser = properties.getProperty("jdbc.user");
        String jdbcPassword = properties.getProperty("jdbc.password");

        Flyway flyway = Flyway.configure()
                .dataSource(jdbcUrl, jdbcUser, jdbcPassword)
                .load();
        flyway.migrate();
    }
}
