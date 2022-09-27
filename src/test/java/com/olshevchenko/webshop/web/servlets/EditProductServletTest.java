package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.entity.Product;
import com.olshevchenko.webshop.service.ProductService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * @author Oleksandr Shevchenko
 */
@ExtendWith(MockitoExtension.class)
class EditProductServletTest {
    private static Product expectedProduct;
    private final EditProductServlet editProductServlet = new EditProductServlet();

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
    void testDoGet() throws IOException {
        EditProductServlet editProductServletMock = mock(EditProductServlet.class);

        doNothing().when(editProductServletMock).doGet(isA(HttpServletRequest.class), isA(HttpServletResponse.class));
        editProductServletMock.doGet(requestMock, responseMock);

        verify(editProductServletMock, times(1)).doGet(requestMock, responseMock);
    }

    @Test
    void testDoPost() {
        EditProductServlet editProductServletMock = mock(EditProductServlet.class);

        doNothing().when(editProductServletMock).doPost(isA(HttpServletRequest.class), isA(HttpServletResponse.class));
        editProductServletMock.doPost(requestMock, responseMock);

        verify(editProductServletMock, times(1)).doPost(requestMock, responseMock);
    }

    @Test
    void testValidateAndGetProduct() {
        when(requestMock.getParameter("id")).thenReturn(String.valueOf(expectedProduct.getId()));
        when(requestMock.getParameter("name")).thenReturn(expectedProduct.getName());
        when(requestMock.getParameter("description")).thenReturn(expectedProduct.getDescription());
        when(requestMock.getParameter("price")).thenReturn(String.valueOf(expectedProduct.getPrice()));

        Optional<Product> optionalExpectedProduct = Optional.ofNullable(expectedProduct);
        Optional<Product> optionalActualProduct = editProductServlet.validateAndGetProduct(requestMock, responseMock, new HashMap<>(), new HashMap<>());
        assertEquals(optionalExpectedProduct, optionalActualProduct);
    }

    @Test
    void testValidateAndGetProductIfNameIsEmpty() throws IOException {
        when(requestMock.getParameter("id")).thenReturn(String.valueOf(expectedProduct.getId()));
        when(requestMock.getParameter("name")).thenReturn("");
        when(requestMock.getParameter("description")).thenReturn(expectedProduct.getDescription());
        when(requestMock.getParameter("price")).thenReturn(String.valueOf(expectedProduct.getPrice()));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(responseMock.getWriter()).thenReturn(writer);

        Map<String, Object> oldParameters = new HashMap<>();
        oldParameters.put("product", expectedProduct);

        Optional<Product> optionalExpectedProduct = Optional.empty();
        Optional<Product> optionalActualProduct = editProductServlet.validateAndGetProduct(requestMock, responseMock, new HashMap<>(), oldParameters);
        assertEquals(optionalExpectedProduct, optionalActualProduct);
    }

    @Test
    void testEditProduct() {
        ProductService productServiceMock = mock(ProductService.class);
        doNothing().when(productServiceMock).add(isA(Product.class));
        productServiceMock.add(expectedProduct);
        verify(productServiceMock, times(1)).add(expectedProduct);
    }


}