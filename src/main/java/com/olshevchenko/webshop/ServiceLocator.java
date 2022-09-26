package com.olshevchenko.webshop;

import com.olshevchenko.webshop.dao.jdbc.JdbcProductDao;
import com.olshevchenko.webshop.dao.jdbc.JdbcUserDao;
import com.olshevchenko.webshop.service.CartService;
import com.olshevchenko.webshop.service.ProductService;
import com.olshevchenko.webshop.service.security.SecurityService;
import com.olshevchenko.webshop.service.UserService;
import com.olshevchenko.webshop.utils.DataSourceFactory;
import com.olshevchenko.webshop.utils.PageGenerator;
import com.olshevchenko.webshop.utils.PropertiesReader;

import javax.sql.DataSource;
import java.util.*;

/**
 * @author Oleksandr Shevchenko
 */
public class ServiceLocator {
    private static final Map<Class<?>, Object> CONTEXT = new HashMap<>();

    static {
        PropertiesReader propertiesReader = new PropertiesReader("application.properties");
        Properties properties = propertiesReader.getProperties();
        String HTML_DIR = properties.getProperty("html.directory");
        int cookieTtlMinutes = Integer.parseInt(properties.getProperty("cookie.ttl.minutes"));

        CONTEXT.put(PropertiesReader.class, propertiesReader);

        PageGenerator pageGenerator = new PageGenerator(HTML_DIR);
        CONTEXT.put(PageGenerator.class, pageGenerator);

        DataSourceFactory dataSourceFactory = new DataSourceFactory(properties);
        DataSource dataSource = dataSourceFactory.createDataSource();

        JdbcProductDao jdbcProductDao = new JdbcProductDao(dataSource);
        ProductService productService = new ProductService(jdbcProductDao);
        CONTEXT.put(ProductService.class, productService);

        JdbcUserDao jdbcUserDao = new JdbcUserDao(dataSource);
        UserService userService = new UserService(jdbcUserDao);
        CONTEXT.put(UserService.class, userService);

        SecurityService securityService = new SecurityService(userService, cookieTtlMinutes);
        CONTEXT.put(SecurityService.class, securityService);

        CartService cartService = new CartService(productService);
        CONTEXT.put(CartService.class, cartService);

    }

    public static <T> T get(Class<T> clazz) {
        return clazz.cast(CONTEXT.get(clazz));
    }


}
