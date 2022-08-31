package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.entity.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Oleksandr Shevchenko
 */
@ExtendWith(MockitoExtension.class)
class DeleteProductServletTest {
    private static Product expectedProduct;

    @Mock
    HttpServletRequest requestMock;

    @Mock
    HttpServletResponse responseMock;

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
    void testDoGet() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(responseMock.getWriter()).thenReturn(writer);
        when(requestMock.getParameter("id")).thenReturn(String.valueOf(expectedProduct.getId()));

        new DeleteProductServlet().doGet(requestMock, responseMock);

        assertTrue(stringWriter.toString().contains("<title>Products List</title>"));
    }


}