package com.olshevchenko.webshop.web.filter;

import lombok.Setter;

import java.util.Arrays;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@Setter
public class ExcludedUrls {
    private String urlExclude;

    public List<String> getUrlExcludeList() {
        return Arrays.asList(urlExclude.split(","));
    }
}
