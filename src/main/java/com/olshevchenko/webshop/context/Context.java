package com.olshevchenko.webshop.context;

import com.olshevchenko.ioc.context.ApplicationContext;
import com.olshevchenko.ioc.context.ClassPathApplicationContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
public class Context {
    private static ApplicationContext context;

    static {
        context = new ClassPathApplicationContext("src/main/webapp/WEB-INF/context.xml");
    }

    public Context(ApplicationContext context) {
        this.context = context;
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
