package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.impl.HttpSessionCartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private HttpSession session;
    @Mock
    private Product product;
    @Mock
    private Cart cart;
    @Mock
    private ArrayListProductDao productDao;
    private Long productId = 1L;
    private String stringQuantity = "5";
    private ServletConfig config;

    @InjectMocks
    private ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();

    public ProductDetailsPageServletTest() {
    }

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        productDao = ArrayListProductDao.getInstance();
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(HttpSessionCartService.class.getName() + ".cart")).thenReturn(cart);
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(product.getId()).thenReturn(productId);
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        when(request.getParameter("quantity")).thenReturn(stringQuantity);
        productDao.save(product);
        servlet.init(config);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("product"), eq(product));
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(product.getStock()).thenReturn(15);
        servlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Added to cart successfully");
    }

    @Test
    public void testArgumentsProduct() throws ServletException, IOException {
        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("product"), argumentCaptor.capture());
        assertEquals(product, argumentCaptor.getValue());
    }
}
