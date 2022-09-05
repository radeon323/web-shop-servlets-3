package com.olshevchenko.webshop.web.servlets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

/**
 * @author Oleksandr Shevchenko
 */
@ExtendWith(MockitoExtension.class)
class ShowAllProductsServletTest {

    @Mock
    HttpServletRequest requestMock;

    @Mock
    HttpServletResponse responseMock;

    @Test
    void testDoGet() {
        ShowAllProductsServlet showAllProductsServlet = mock(ShowAllProductsServlet.class);

        doNothing().when(showAllProductsServlet).doGet(isA(HttpServletRequest.class), isA(HttpServletResponse.class));
        showAllProductsServlet.doGet(requestMock, responseMock);

        verify(showAllProductsServlet, times(1)).doGet(requestMock, responseMock);
    }


}