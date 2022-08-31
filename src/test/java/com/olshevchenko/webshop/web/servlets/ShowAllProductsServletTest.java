package com.olshevchenko.webshop.web.servlets;

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
class ShowAllProductsServletTest {

    @Mock
    HttpServletRequest requestMock;

    @Mock
    HttpServletResponse responseMock;

    @Test
    void testDoGet() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(responseMock.getWriter()).thenReturn(writer);

        new ShowAllProductsServlet().doGet(requestMock, responseMock);

        assertTrue(stringWriter.toString().contains("<title>Products List</title>"));
    }


}