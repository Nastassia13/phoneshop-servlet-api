package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.ArgumentIsNullException;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.RecentlyViewedProducts;
import com.es.phoneshop.utils.RecentlyViewedProductsLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpSessionRecentlyViewedProductsServiceTest {
    private HttpSessionRecentlyViewedProductsService service;
    @Mock
    private RecentlyViewedProductsLoader loader;
    @Mock
    private ProductDao productDao;
    @Mock
    private RecentlyViewedProducts viewedProducts;
    @Mock
    LinkedList<Long> products;
    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;

    @Before
    public void setup() {
        service = HttpSessionRecentlyViewedProductsService.getInstance();
        productDao = ArrayListProductDao.getInstance();
        Currency usd = Currency.getInstance("USD");
        product1 = new Product(1L, "test-1", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg");
        product2 = new Product(2L, "test-2", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        product3 = new Product(3L, "test-3", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg");
        product4 = new Product(4L, "test-4", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg");
        products = new LinkedList<>(Arrays.asList(2L, 1L));
        viewedProducts = new RecentlyViewedProducts(products);
        productDao.save(product1);
        productDao.save(product2);
        productDao.save(product3);
        productDao.save(product4);
    }

    @Test
    public void testAddToList() {
        Long productId = 3L;
        when(loader.getViewedProducts()).thenReturn(viewedProducts);
        service.addToList(loader, productId);
        assertEquals(product3.getId(), viewedProducts.getProductsList().get(0));
    }

    @Test(expected = ArgumentIsNullException.class)
    public void testAddToListNull() {
        Long productId = null;
        service.addToList(loader, productId);
        assertEquals(product3.getId(), viewedProducts.getProductsList().get(0));
        fail("Expected ArgumentIsNullException");
    }

    @Test
    public void testAddToListOverSize() {
        Long productId1 = 3L;
        Long productId2 = 4L;
        when(loader.getViewedProducts()).thenReturn(viewedProducts);
        service.addToList(loader, productId1);
        service.addToList(loader, productId2);
        assertEquals(product4.getId(), viewedProducts.getProductsList().get(0));
        assertEquals(product2.getId(), viewedProducts.getProductsList().get(2));
    }

    @Test
    public void testAddToListSecondTime() {
        Long productId1 = 3L;
        Long productId2 = 2L;
        when(loader.getViewedProducts()).thenReturn(viewedProducts);
        service.addToList(loader, productId1);
        service.addToList(loader, productId2);
        assertEquals(product3.getId(), viewedProducts.getProductsList().get(1));
        assertEquals(product2.getId(), viewedProducts.getProductsList().get(0));
    }
}
