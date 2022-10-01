package com.olshevchenko.webshop.context;

import com.olshevchenko.ioc.context.ApplicationContext;
import com.olshevchenko.ioc.context.ClassPathApplicationContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@AllArgsConstructor
public class Context {
    private static final ApplicationContext context;

    static {
        context = new ClassPathApplicationContext("src/main/webapp/WEB-INF/context.xml");
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
