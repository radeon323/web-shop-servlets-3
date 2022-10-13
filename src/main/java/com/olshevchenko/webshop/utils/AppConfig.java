package com.olshevchenko.webshop.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@Configuration
@PropertySource("classpath:/application.properties")
public class AppConfig {

    @Bean
    public List<String> allowedPaths(@Value("${security.filter.url.exclude}") String paths) {
        return Arrays.asList(paths.split(","));
    }

    @Bean
    public DataSource dataSource(@Value("${jdbc.url}") String jdbcUrl,
                                 @Value("${jdbc.user}") String jdbcUser,
                                 @Value("${jdbc.password}") String jdbcPassword) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUser);
        dataSource.setPassword(jdbcPassword);
        return dataSource;
    }

    @Bean
    public FreeMarkerViewResolver freemarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(true);
        resolver.setPrefix("");
        resolver.setSuffix(".html");
        return resolver;
    }

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("classpath:templates/");
        return freeMarkerConfigurer;
    }
}
