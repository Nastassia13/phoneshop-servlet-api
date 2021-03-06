package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.impl.HttpSessionCartService;
import com.es.phoneshop.service.impl.HttpSessionOrderService;
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
import java.math.BigDecimal;
import java.util.Currency;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private HttpSession session;
    private Cart cart;
    private Order order;
    private OrderService orderService;
    private OrderDao orderDao;
    private ServletConfig config;

    @InjectMocks
    private CheckoutPageServlet servlet = new CheckoutPageServlet();

    public CheckoutPageServletTest() {
    }

    @Before
    public void setup() throws ServletException {
        orderDao = ArrayListOrderDao.getInstance();
        orderService = HttpSessionOrderService.getInstance();
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        cart = new Cart();
        order = new Order();
        Currency usd = Currency.getInstance("USD");
        Product product1 = new Product(1L, "test-1", "Samsung Galaxy S", new BigDecimal(300), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product product2 = new Product(2L, "test-2", "Samsung Galaxy S II", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        cart.getItems().add(new CartItem(product1, 2));
        cart.getItems().add(new CartItem(product2, 3));
        cart.setTotalCost(BigDecimal.valueOf(1200));
        when(request.getSession().getAttribute(HttpSessionCartService.class.getName() + ".cart")).thenReturn(cart);
        order = orderService.getOrder(cart);
        orderDao.save(order);
        servlet.init(config);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        request.setAttribute("paymentMethods", PaymentMethod.values());
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("Aaa");
        when(request.getParameter("lastName")).thenReturn("Bbb");
        when(request.getParameter("deliveryDate")).thenReturn("2021-03-03");
        when(request.getParameter("deliveryAddress")).thenReturn("Ccc");
        when(request.getParameter("phone")).thenReturn("+375293632323");
        when(request.getParameter("payment")).thenReturn("CACHE");
        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }
}
