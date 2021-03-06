package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.PriceHistory;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductPriceHistoryPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private Product product;
    @Mock
    private List<PriceHistory> priceHistory;
    @Mock
    private ArrayListProductDao productDao;
    private Long productId;
    private ServletConfig config;

    @InjectMocks
    private ProductPriceHistoryPageServlet servlet = new ProductPriceHistoryPageServlet();

    public ProductPriceHistoryPageServletTest() {
    }

    @Before
    public void setup() throws ServletException {
        productId = 1L;
        productDao = ArrayListProductDao.getInstance();
        priceHistory = new ArrayList<>();
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(product.getId()).thenReturn(productId);
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(product.getHistory()).thenReturn(priceHistory);
        product.appendPrice(new BigDecimal(500));
        productDao.save(product);
        servlet.init(config);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("product"), eq(product));
        verify(request).setAttribute(eq("history"), eq(priceHistory));
    }

    @Test
    public void testArgumentsProduct() throws ServletException, IOException {
        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("product"), argumentCaptor.capture());
        assertEquals(product, argumentCaptor.getValue());
    }

    @Test
    public void testArgumentsHistory() throws ServletException, IOException {
        ArgumentCaptor<ArrayList<PriceHistory>> argumentCaptor = ArgumentCaptor.forClass(ArrayList.class);
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("history"), argumentCaptor.capture());
        assertEquals(priceHistory, argumentCaptor.getValue());
    }
}