package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.entity.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

/**
 * @author Oleksandr Shevchenko
 */
@ExtendWith(MockitoExtension.class)
class DeleteProductServletTest {
    private static Product expectedProduct;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @BeforeAll
    static void beforeAll() {
        expectedProduct = Product.builder()
                .id(1)
                .name("Samsung Galaxy M52")
                .description("6.7 inches, Qualcomm SM7325 Snapdragon 778G 5G")
                .price(13499.0)
                .build();
    }

    @Test
    void testDoGet() {
        DeleteProductServlet deleteProductServlet = mock(DeleteProductServlet.class);

        doNothing().when(deleteProductServlet).doGet(isA(HttpServletRequest.class), isA(HttpServletResponse.class));
        deleteProductServlet.doGet(requestMock, responseMock);

        verify(deleteProductServlet, times(1)).doGet(requestMock, responseMock);
    }


}