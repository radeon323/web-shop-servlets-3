package com.olshevchenko.webshop.service;

import com.olshevchenko.webshop.dao.ProductDao;
import com.olshevchenko.webshop.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

/**
 * @author Oleksandr Shevchenko
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDaoMock;

    private ProductService productService;

    @BeforeEach
    void init() {
        productService = new ProductService(productDaoMock);
    }

    @Test
    void testFindAll() {
        List<Product> products = new ArrayList<>();
        Product productSamsung = Product.builder()
                .id(1)
                .name("Samsung Galaxy M52")
                .description("6.7 inches, Qualcomm SM7325 Snapdragon 778G 5G")
                .price(13499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        products.add(productSamsung);
        Product productXiaomi = Product.builder()
                .id(2)
                .name("Xiaomi Redmi Note 9 Pro")
                .description("6.67 inches, Qualcomm SM7125 Snapdragon 720G Octa-core")
                .price(11699.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        products.add(productXiaomi);

        when(productDaoMock.findAll()).thenReturn(products);
        List<Product> actualProducts = productService.findAll();
        assertNotNull(actualProducts);
        assertEquals(2, actualProducts.size());
        assertEquals(productSamsung, actualProducts.get(0));
        assertEquals(productXiaomi, actualProducts.get(1));
        verify(productDaoMock, times(1)).findAll();
    }

    @Test
    void testFindById() {
        List<Product> products = new ArrayList<>();
        Product productSamsung = Product.builder()
                .id(1)
                .name("Samsung Galaxy M52")
                .description("6.7 inches, Qualcomm SM7325 Snapdragon 778G 5G")
                .price(13499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        products.add(productSamsung);
        Product productXiaomi = Product.builder()
                .id(2)
                .name("Xiaomi Redmi Note 9 Pro")
                .description("6.67 inches, Qualcomm SM7125 Snapdragon 720G Octa-core")
                .price(11699.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        products.add(productXiaomi);

        when(productDaoMock.findById(1)).thenReturn(Optional.ofNullable(products.get(0)));
        Optional<Product> actualProduct = productService.findById(1);
        assertTrue(actualProduct.isPresent());
        assertEquals(productSamsung, actualProduct.get());
        verify(productDaoMock, times(1)).findById(1);
    }

    @Test
    void testAdd() {
        Product productSamsung = Product.builder()
                .id(1)
                .name("Samsung Galaxy M52")
                .description("6.7 inches, Qualcomm SM7325 Snapdragon 778G 5G")
                .price(13499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        doNothing().when(productDaoMock).add(isA(Product.class));
        productDaoMock.add(productSamsung);
        verify(productDaoMock, times(1)).add(productSamsung);
    }

    @Test
    void testRemove() {
        doNothing().when(productDaoMock).remove(isA(Integer.class));
        productDaoMock.remove(1);
        verify(productDaoMock, times(1)).remove(1);
    }

    @Test
    void testUpdate() {
        Product productSamsung = Product.builder()
                .id(1)
                .name("Samsung Galaxy M52")
                .description("6.7 inches, Qualcomm SM7325 Snapdragon 778G 5G")
                .price(13499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        doNothing().when(productDaoMock).update(isA(Product.class));
        productDaoMock.update(productSamsung);
        verify(productDaoMock, times(1)).update(productSamsung);
    }


}