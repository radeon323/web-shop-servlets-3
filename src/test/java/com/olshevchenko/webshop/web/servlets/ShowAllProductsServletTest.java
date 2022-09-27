package com.olshevchenko.webshop.web.servlets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

/**
 * @author Oleksandr Shevchenko
 */
@ExtendWith(MockitoExtension.class)
class ShowAllProductsServletTest {

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Test
    void testDoGet() throws IOException {
        ShowAllProductsServlet showAllProductsServlet = mock(ShowAllProductsServlet.class);

        doNothing().when(showAllProductsServlet).doGet(isA(HttpServletRequest.class), isA(HttpServletResponse.class));
        showAllProductsServlet.doGet(requestMock, responseMock);

        verify(showAllProductsServlet, times(1)).doGet(requestMock, responseMock);
    }


}