package com.olshevchenko.webshop.dao.jdbc;

import com.olshevchenko.webshop.entity.Product;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Oleksandr Shevchenko
 */
class ProductRowMapperTest {
    private final Product expectedProduct = Product.builder()
                                                .id(1)
                                                .name("Samsung Galaxy M52")
                                                .description("6.7 inches, Qualcomm SM7325 Snapdragon 778G 5G")
                                                .price(13499.0)
                                                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                                                .build();

    @Test
    void testMapRow() throws SQLException {
        ProductRowMapper productRowMapper = new ProductRowMapper();

        ResultSet resultSetMock = mock(ResultSet.class);
        when(resultSetMock.getInt("id")).thenReturn(expectedProduct.getId());
        when(resultSetMock.getString("name")).thenReturn(expectedProduct.getName());
        when(resultSetMock.getString("description")).thenReturn(expectedProduct.getDescription());
        when(resultSetMock.getDouble("price")).thenReturn(expectedProduct.getPrice());
        when(resultSetMock.getTimestamp("creation_date")).thenReturn(Timestamp.valueOf(expectedProduct.getCreationDate()));

        Product actualProduct = productRowMapper.mapRow(resultSetMock, 0);

        assertEquals(expectedProduct, actualProduct);
    }
}