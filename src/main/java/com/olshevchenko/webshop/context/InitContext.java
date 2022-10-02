package com.olshevchenko.webshop.context;

import com.olshevchenko.ioc.context.ApplicationContext;
import com.olshevchenko.ioc.context.ClassPathApplicationContext;
import com.olshevchenko.webshop.utils.PropertiesReader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@AllArgsConstructor
public class InitContext {
    private static final ApplicationContext context;

    static {
        PropertiesReader propertiesReader = new PropertiesReader("application.properties");
        String path = propertiesReader.getProperties().getProperty("context.path");
        log.info("Starting context container...");
        context = new ClassPathApplicationContext(path);
        for (String beanName : context.getBeanNames()) {
            log.info("Creating bean '{}'", beanName);
        }
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
