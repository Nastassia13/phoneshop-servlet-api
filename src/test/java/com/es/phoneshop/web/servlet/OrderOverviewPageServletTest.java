package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.model.order.Order;
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
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderOverviewPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    private OrderDao orderDao;
    private Order order;
    private ServletConfig config;

    @InjectMocks
    private OrderOverviewPageServlet servlet = new OrderOverviewPageServlet();

    public OrderOverviewPageServletTest() {
    }

    @Before
    public void setup() throws ServletException {
        orderDao = ArrayListOrderDao.getInstance();
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        String secureId = UUID.randomUUID().toString();
        order = new Order();
        order.setSecureId(secureId);
        orderDao.save(order);
        when(request.getPathInfo()).thenReturn("/" + secureId);
        servlet.init(config);
    }

    @Test
    public void testDoGet() throws IOException, ServletException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("order"), eq(order));
    }

    @Test
    public void testArgumentsOrder() throws ServletException, IOException {
        ArgumentCaptor<Order> argumentCaptor = ArgumentCaptor.forClass(Order.class);
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("order"), argumentCaptor.capture());
        assertEquals(order, argumentCaptor.getValue());
    }
}
