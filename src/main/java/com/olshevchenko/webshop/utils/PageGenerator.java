package com.olshevchenko.webshop.utils;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@RequiredArgsConstructor
public class PageGenerator {
    private final String HTML_DIR;

    public String getPage(String fileName, Map<String, Object> data) {
        try {
            Writer stream = new StringWriter();
            Template template = initConfiguration().getTemplate(fileName);
            template.process(data, stream);
            return stream.toString();
        } catch (IOException | TemplateException e) {
            log.error("Cannot generate page {}", fileName, e);
            throw new RuntimeException("Cannot generate page " + fileName, e);
        }
    }

    public String getPage(String filename) {
        return getPage(filename, Collections.emptyMap());
    }

    private Configuration initConfiguration() {
        Configuration config = new Configuration(Configuration.VERSION_2_3_31);
        final ClassTemplateLoader loader = new ClassTemplateLoader(PageGenerator.class, HTML_DIR);
        config.setTemplateLoader(loader);
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return config;
    }


}
