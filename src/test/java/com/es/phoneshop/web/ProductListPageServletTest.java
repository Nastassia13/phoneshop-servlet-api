package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.impl.HttpSessionCartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ArrayListProductDao productDao;
    @Mock
    private List<Product> products;
    @Mock
    private Product product1;
    @Mock
    private Product product2;
    @Mock
    private Product product3;
    private String[] productId = new String[]{"1", "2", "3"};
    private String[] stringQuantity = new String[]{"5", "2", "3"};

    @InjectMocks
    private ProductListPageServlet servlet = new ProductListPageServlet();

    public ProductListPageServletTest() {
    }

    @Before
    public void setup() throws ServletException {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(productDao.findProducts(null, null, null)).thenReturn(products);
        when(request.getParameterValues("quantity")).thenReturn(stringQuantity);
        when(request.getParameterValues("productId")).thenReturn(productId);
        productDao.save(product1);
        productDao.save(product2);
        productDao.save(product3);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("products"), eq(products));
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        servlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + "/products?message=Added to cart successfully");
    }
}