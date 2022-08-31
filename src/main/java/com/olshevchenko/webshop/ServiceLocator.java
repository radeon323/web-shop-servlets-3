package com.olshevchenko.webshop;

import com.olshevchenko.webshop.dao.jdbc.JdbcProductDao;
import com.olshevchenko.webshop.service.ProductService;
import com.olshevchenko.webshop.web.utils.DataSourceFactory;
import com.olshevchenko.webshop.web.utils.PageGenerator;
import com.olshevchenko.webshop.web.utils.PropertiesReader;

import javax.sql.DataSource;
import java.util.*;

/**
 * @author Oleksandr Shevchenko
 */
public class ServiceLocator {
    private static final Map<Class<?>, Object> CONTEXT = new HashMap<>();

    static {
        Properties properties = new PropertiesReader().getProperties("application.properties");
        String HTML_DIR = properties.getProperty("html.directory");

        PageGenerator pageGenerator = new PageGenerator(HTML_DIR);
        CONTEXT.put(PageGenerator.class, pageGenerator);

        DataSourceFactory dataSourceFactory = new DataSourceFactory(properties);
        DataSource dataSource = dataSourceFactory.createDataSource();

        JdbcProductDao jdbcProductDao = new JdbcProductDao(dataSource);

        ProductService productService = new ProductService(jdbcProductDao);
        CONTEXT.put(ProductService.class, productService);

    }

    public static <T> T get(Class<T> clazz) {
        return clazz.cast(CONTEXT.get(clazz));
    }





}
