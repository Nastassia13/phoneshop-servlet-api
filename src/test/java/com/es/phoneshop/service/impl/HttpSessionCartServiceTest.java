package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.ArgumentIsNullException;
import com.es.phoneshop.exception.OutOfQuantityException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.utils.CartLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class HttpSessionCartServiceTest {
    private HttpSessionCartService service;
    @Mock
    private CartLoader cartLoader;
    @Mock
    private Cart cart;
    @Mock
    private ProductDao productDao;
    private List<CartItem> items;
    private Product product1;
    private CartItem item1;

    @Before
    public void setup() {
        service = HttpSessionCartService.getInstance();
        productDao = ArrayListProductDao.getInstance();
        Currency usd = Currency.getInstance("USD");
        product1 = new Product(1L, "test-1", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg");
        Product product2 = new Product(2L, "test-2", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Product product3 = new Product(3L, "test-3", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg");
        item1 = new CartItem(product1, 3);
        items = new ArrayList<>(Arrays.asList(item1, new CartItem(product2, 5)));
        productDao.save(product1);
        productDao.save(product2);
        productDao.save(product3);
        when(cart.getItems()).thenReturn(items);
    }

    @Test
    public void testGetCart() {
        when(cartLoader.getCart()).thenReturn(cart);
        assertEquals(cart, service.getCart(cartLoader));
    }

    @Test(expected = ArgumentIsNullException.class)
    public void testGetCartNullCartLoader() throws ProductNotFoundException {
        cartLoader = null;
        assertEquals(cart, service.getCart(cartLoader));
        fail("Expected ArgumentIsNullException");
    }

    @Test
    public void testAddPositive() throws OutOfQuantityException, OutOfStockException {
        Long productId = 3L;
        int quantity = 2;
        service.add(cart, productId, quantity);
        assertEquals(quantity, cart.getItems().get(2).getQuantity());
    }

    @Test(expected = OutOfQuantityException.class)
    public void testAddNegative() throws OutOfQuantityException, OutOfStockException {
        Long productId = 3L;
        int quantity = -2;
        service.add(cart, productId, quantity);
        assertEquals(quantity, cart.getItems().get(2).getQuantity());
        fail("Expected OutOfQuantityException");
    }

    @Test(expected = OutOfStockException.class)
    public void testAddPositiveOverStock() throws OutOfQuantityException, OutOfStockException {
        Long productId = 3L;
        int quantity = 100;
        service.add(cart, productId, quantity);
        assertEquals(quantity, cart.getItems().get(2).getQuantity());
        fail("Expected OutOfStockException");
    }

    @Test
    public void testAddPositiveToExisting() throws OutOfQuantityException, OutOfStockException {
        Long productId = 1L;
        int quantity = 2;
        int expectedQuantity = items.get(0).getQuantity() + quantity;
        service.add(cart, productId, quantity);
        assertEquals(expectedQuantity, cart.getItems().get(0).getQuantity());
    }

    @Test(expected = OutOfStockException.class)
    public void testAddPositiveToExistingOverStock() throws OutOfQuantityException, OutOfStockException {
        Long productId = 1L;
        int quantity = 5;
        int expectedQuantity = items.get(0).getQuantity() + quantity;
        service.add(cart, productId, quantity);
        assertEquals(expectedQuantity, cart.getItems().get(0).getQuantity());
        fail("Expected OutOfStockException");
    }

    @Test
    public void testAddNegativeToExisting() throws OutOfQuantityException, OutOfStockException {
        Long productId = 1L;
        int quantity = -2;
        int expectedQuantity = items.get(0).getQuantity() + quantity;
        service.add(cart, productId, quantity);
        assertEquals(expectedQuantity, cart.getItems().get(0).getQuantity());
    }

    @Test(expected = OutOfQuantityException.class)
    public void testAddNegativeToExistingOverQuantity() throws OutOfQuantityException, OutOfStockException {
        Long productId = 1L;
        int quantity = -5;
        int expectedQuantity = items.get(0).getQuantity() + quantity;
        service.add(cart, productId, quantity);
        assertEquals(expectedQuantity, cart.getItems().get(0).getQuantity());
        fail("Expected OOutOfQuantityException");
    }

    @Test
    public void testAddNegativeToExistingWithDeleting() throws OutOfQuantityException, OutOfStockException {
        Long productId = 1L;
        int quantity = -3;
        service.add(cart, productId, quantity);
        assertFalse(cart.getItems().contains(item1));
    }
}
