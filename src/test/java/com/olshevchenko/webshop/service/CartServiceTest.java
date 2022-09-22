package com.olshevchenko.webshop.service;

import com.olshevchenko.webshop.entity.CartItem;
import com.olshevchenko.webshop.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Oleksandr Shevchenko
 */
@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private ProductService productServiceMock;

    private CartService cartService;
    private List<CartItem> items;
    private Product productSamsung;
    private Product productXiaomi;
    private CartItem itemSamsung;
    private CartItem itemXiaomi;

    @BeforeEach
    void init() {
        cartService = new CartService(productServiceMock);

        items = new ArrayList<>();
        productSamsung = Product.builder()
                .id(1)
                .name("Samsung Galaxy M52")
                .description("6.7 inches, Qualcomm SM7325 Snapdragon 778G 5G")
                .price(13499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        itemSamsung = new CartItem(productSamsung, 2);
        items.add(itemSamsung);
        productXiaomi = Product.builder()
                .id(2)
                .name("Xiaomi Redmi Note 9 Pro")
                .description("6.67 inches, Qualcomm SM7125 Snapdragon 720G Octa-core")
                .price(11699.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        itemXiaomi = new CartItem(productXiaomi, 3);
        items.add(itemXiaomi);
    }

    @Test
    void testAddToCart() {
        when(productServiceMock.findById(1)).thenReturn(productSamsung);
        when(productServiceMock.findById(2)).thenReturn(productXiaomi);

        assertEquals(2, items.get(0).getQuantity());
        cartService.addToCart(items,1);
        assertEquals(3, items.get(0).getQuantity());

        assertEquals(3, items.get(1).getQuantity());
        cartService.addToCart(items,2);
        assertEquals(4, items.get(1).getQuantity());
    }

    @Test
    void testAddToCartIfCartIsEmpty() {
        List<CartItem> items = new ArrayList<>();

        when(productServiceMock.findById(1)).thenReturn(productSamsung);
        when(productServiceMock.findById(2)).thenReturn(productXiaomi);

        assertTrue(items.isEmpty());
        cartService.addToCart(items,1);
        assertEquals(1, items.get(0).getQuantity());
        assertEquals(productSamsung, items.get(0).getProduct());

        cartService.addToCart(items,2);
        assertEquals(1, items.get(1).getQuantity());
        assertEquals(productXiaomi, items.get(1).getProduct());
    }

    @Test
    void testRemoveFromCart() {
        assertTrue(items.contains(itemSamsung));
        cartService.removeFromCart(items,1);
        assertFalse(items.contains(itemSamsung));

        assertTrue(items.contains(itemXiaomi));
        cartService.removeFromCart(items,2);
        assertFalse(items.contains(itemXiaomi));
    }

    @Test
    void testUpdateQuantity() {
        assertEquals(2, items.get(0).getQuantity());
        cartService.updateQuantity(items,1,10);
        assertEquals(10, items.get(0).getQuantity());

        assertEquals(3, items.get(1).getQuantity());
        cartService.updateQuantity(items,2,8);
        assertEquals(8, items.get(1).getQuantity());
    }

    @Test
    void testCalculateTotalPrice() {
        double total = cartService.calculateTotalPrice(items);
        assertEquals(62095.0, total);
    }
}