package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
    @Mock
    private ArrayListProductDao productDao;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private HttpSession session;
    @Mock
    private Product product1;
    @Mock
    private Product product2;
    @Mock
    private Product product3;
    private String[] productId = new String[]{"1", "2", "3"};
    private String[] stringQuantity = new String[]{"5", "2", "3"};
    private ServletConfig config;

    @InjectMocks
    private CartPageServlet servlet = new CartPageServlet();

    public CartPageServletTest() {
    }

    @Before
    public void setup() throws ServletException {
        productDao = ArrayListProductDao.getInstance();
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(product1.getId()).thenReturn(1L);
        when(product2.getId()).thenReturn(2L);
        when(product3.getId()).thenReturn(3L);
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        when(request.getParameterValues("quantity")).thenReturn(stringQuantity);
        when(request.getParameterValues("productId")).thenReturn(productId);
        productDao.save(product1);
        productDao.save(product2);
        productDao.save(product3);
        servlet.init(config);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        servlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + "/cart?message=Cart updated successfully");
    }
}
