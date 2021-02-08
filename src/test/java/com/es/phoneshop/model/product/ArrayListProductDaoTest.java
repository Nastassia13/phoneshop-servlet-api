package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ArrayListProductDao productDao;
    private Currency usd;
    private Long id;
    private Product product1;
    private Product product2;

    @Before
    public void setup() throws ProductNotFoundException {
        productDao = new ArrayListProductDao();
        usd = Currency.getInstance("USD");
        id = 100L;
        product1 = new Product(id, "test-1", "Samsung Galaxy S", null, usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        product2 = new Product(101L, "test-2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        productDao.save(product1);
        productDao.save(product2);
    }

    @Test
    public void testGetProduct() throws ProductNotFoundException {
        assertEquals(product1, productDao.getProduct(id));
    }

    @Test(expected = ProductNotFoundException.class)
    public void testGetProductNotFound() throws ProductNotFoundException {
        Long id = 0L;
        productDao.getProduct(id);
        fail("Expected ProductNotFoundException");
    }

    @Test(expected = ProductNotFoundException.class)
    public void testGetProductNull() throws ProductNotFoundException {
        Long id = null;
        assertEquals(product1, productDao.getProduct(id));
        fail("Expected ProductNotFoundException");
    }

    @Test
    public void testFindProductsHaveResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testFindProductsWithNullPrice() {
        assertFalse(productDao.findProducts().contains(product1));
    }

    @Test
    public void testFindProductsWithZeroStock() {
        assertFalse(productDao.findProducts().contains(product2));
    }

    @Test
    public void testSaveProduct() throws ProductNotFoundException {
        Product product = new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        Long id = product.getId();
        assertNotNull(id);
        assertEquals(product, productDao.getProduct(id));
    }

    @Test
    public void testSaveProductWithId() throws ProductNotFoundException {
        Long id = 102L;
        Product product = new Product(id, "test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        assertEquals(product, productDao.getProduct(id));
    }

    @Test
    public void testSaveProductWithExistingId() throws ProductNotFoundException {
        Product product = new Product(id, "test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        assertEquals(product, productDao.getProduct(id));
    }

    @Test(expected = ProductNotFoundException.class)
    public void testSaveProductNull() throws ProductNotFoundException {
        Product product = null;
        productDao.save(product);
        fail("Expected ProductNotFoundException");
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteProduct() throws ProductNotFoundException {
        productDao.delete(id);
        productDao.getProduct(id);
        fail("Expected ProductNotFoundException");
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteProductNull() throws ProductNotFoundException {
        Long id = null;
        productDao.delete(id);
        fail("Expected ProductNotFoundException");
    }
}
