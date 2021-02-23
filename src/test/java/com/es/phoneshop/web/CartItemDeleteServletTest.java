package com.es.phoneshop.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartItemDeleteServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    private ServletConfig config;

    @InjectMocks
    private CartItemDeleteServlet servlet = new CartItemDeleteServlet();

    public CartItemDeleteServletTest() {
    }

    @Before
    public void setup() throws ServletException {
        Long productId = 1L;
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(request.getSession()).thenReturn(session);
        servlet.init(config);
    }

    @Test
    public void testDoPost() throws IOException {
        servlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + "/cart?message=Cart item removed successfully");
    }
}
