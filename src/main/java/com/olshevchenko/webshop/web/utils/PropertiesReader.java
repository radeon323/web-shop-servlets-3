package com.olshevchenko.webshop.web.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
public class PropertiesReader {
    private final Map<String, Properties> cachedProperties = new ConcurrentHashMap<>();

    public Properties getProperties(String path)  {
        if (!cachedProperties.containsKey(path)) {
            cachedProperties.put(path, readProperties(path));
        }
        return new Properties(cachedProperties.get(path));
    }

    private Properties readProperties(String path)  {
        Properties properties = new Properties();
        try (InputStream inputStream = PropertiesReader.class.getClassLoader().getResourceAsStream(path);
             BufferedInputStream resource = new BufferedInputStream(Objects.requireNonNull(inputStream))) {
            properties.load(resource);
            return properties;
        } catch (IOException e) {
            log.error("Cannot read properties from file: {} ", path, e);
            throw new RuntimeException("Cannot read properties from file: " + path, e);
        }
    }


}
