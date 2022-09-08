package com.olshevchenko.webshop.web.servlets;

import com.olshevchenko.webshop.entity.Product;
import com.olshevchenko.webshop.service.ProductService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author Oleksandr Shevchenko
 */
@ExtendWith(MockitoExtension.class)
class AddProductServletTest {
    private static Product expectedProduct;
    private final AddProductServlet addProductServlet = new AddProductServlet();

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @BeforeAll
    static void beforeAll() {
        expectedProduct = Product.builder()
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

        addProductServlet.doGet(requestMock, responseMock);

        assertTrue(stringWriter.toString().contains("<title>Add Product</title>"));
    }

    @Test
    void testDoPost() {
        AddProductServlet addProductServletMock = mock(AddProductServlet.class);

        doNothing().when(addProductServletMock).doPost(isA(HttpServletRequest.class), isA(HttpServletResponse.class));
        addProductServletMock.doPost(requestMock, responseMock);

        verify(addProductServletMock, times(1)).doPost(requestMock, responseMock);
    }

    @Test
    void testValidateAndGetProduct() {
        when(requestMock.getParameter("name")).thenReturn(expectedProduct.getName());
        when(requestMock.getParameter("description")).thenReturn(expectedProduct.getDescription());
        when(requestMock.getParameter("price")).thenReturn(String.valueOf(expectedProduct.getPrice()));

        Optional<Product> optionalExpectedProduct = Optional.ofNullable(expectedProduct);
        Optional<Product> optionalActualProduct = addProductServlet.validateAndGetProduct(requestMock, responseMock);
        assertEquals(optionalExpectedProduct, optionalActualProduct);
    }

    @Test
    void testValidateAndGetProductIfNameIsEmpty() throws IOException {
        when(requestMock.getParameter("name")).thenReturn("");
        when(requestMock.getParameter("description")).thenReturn(expectedProduct.getDescription());
        when(requestMock.getParameter("price")).thenReturn(String.valueOf(expectedProduct.getPrice()));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(responseMock.getWriter()).thenReturn(writer);

        Optional<Product> optionalExpectedProduct = Optional.empty();
        Optional<Product> optionalActualProduct = addProductServlet.validateAndGetProduct(requestMock, responseMock);
        assertEquals(optionalExpectedProduct, optionalActualProduct);
    }

    @Test
    void testAddProduct() {
        ProductService productServiceMock = mock(ProductService.class);
        doNothing().when(productServiceMock).add(isA(Product.class));
        productServiceMock.add(expectedProduct);
        verify(productServiceMock, times(1)).add(expectedProduct);
    }


}