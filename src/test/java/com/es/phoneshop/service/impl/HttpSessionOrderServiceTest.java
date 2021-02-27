package com.es.phoneshop.service.impl;

import com.es.phoneshop.exception.ArgumentIsNullException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.utils.CartLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpSessionOrderServiceTest {
    @Mock
    private Cart cart;
    private List<CartItem> items;
    private CartItem item1;
    private CartItem item2;

    @InjectMocks
    private HttpSessionOrderService service;

    @Before
    public void setup() {
        service = HttpSessionOrderService.getInstance();
        Currency usd = Currency.getInstance("USD");
        Product product1 = new Product(1L, "test-1", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg");
        Product product2 = new Product(2L, "test-2", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        item1 = new CartItem(product1, 1);
        item2 = new CartItem(product2, 2);
        items = new ArrayList<>(Arrays.asList(item1, item2));
        when(cart.getItems()).thenReturn(items);
        when(cart.getTotalCost()).thenReturn(BigDecimal.valueOf(700));
    }

    @Test
    public void testGetOrder() {
        Order order = service.getOrder(cart);
        assertEquals(BigDecimal.valueOf(700), order.getSubtotal());
        assertEquals(BigDecimal.valueOf(5), order.getDeliveryCost());
        assertEquals(BigDecimal.valueOf(705), order.getTotalCost());
    }

    @Test(expected = ArgumentIsNullException.class)
    public void testGetOrderNullCart() {
        cart = null;
        assertEquals(items, service.getOrder(cart).getItems());
        fail("Expected ArgumentIsNullException");
    }

    @Test
    public void testGetPaymentMethod() {
        List<PaymentMethod> methods = Arrays.asList(PaymentMethod.CACHE, PaymentMethod.CREDIT_CART);
        assertEquals(methods, service.getPaymentMethod());
    }

    @Test
    public void testPlaceOrder() {
        Order order = new Order();
        service.placeOrder(order);
        assertNotNull(order.getId());
        assertNotNull(order.getSecureId());
    }
}
