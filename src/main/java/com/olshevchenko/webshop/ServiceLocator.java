package com.olshevchenko.webshop;

import com.olshevchenko.webshop.dao.jdbc.JdbcProductDao;
import com.olshevchenko.webshop.dao.jdbc.JdbcUserDao;
import com.olshevchenko.webshop.service.CartService;
import com.olshevchenko.webshop.service.ProductService;
import com.olshevchenko.webshop.service.SecurityService;
import com.olshevchenko.webshop.service.UserService;
import com.olshevchenko.webshop.web.filter.SecurityFilter;
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
        int cookieLifeTime = Integer.parseInt(properties.getProperty("cookie.ttl.seconds"));
        List<String> allowedPaths = Arrays.asList(properties.getProperty("security.filter.url.exclude").split(","));

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

        SecurityService securityService = new SecurityService(cookieLifeTime);
        CONTEXT.put(SecurityService.class, securityService);

        CartService cartService = new CartService(productService);
        CONTEXT.put(CartService.class, cartService);

        SecurityFilter securityFilter = new SecurityFilter();
        CONTEXT.put(SecurityFilter.class, securityFilter);
    }

    public static <T> T get(Class<T> clazz) {
        return clazz.cast(CONTEXT.get(clazz));
    }


}
