package com.olshevchenko.webshop.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@RequiredArgsConstructor
public class PropertiesReader {
    private final Map<String, Properties> cachedProperties = new ConcurrentHashMap<>();
    private final String path;

    public Properties getProperties()  {
        if (!cachedProperties.containsKey(path)) {
            cachedProperties.put(path, readProperties(path));
        }
        return new Properties(cachedProperties.get(path));
    }

    public List<String> getAllowedUriPaths() {
        return Arrays.asList(getProperties().getProperty("security.filter.url.exclude").split(","));
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
