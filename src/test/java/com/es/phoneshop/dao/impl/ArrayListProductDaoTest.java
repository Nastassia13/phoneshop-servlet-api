package com.es.phoneshop.dao.impl;

import com.es.phoneshop.exception.ArgumentIsNullException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ArrayListProductDao productDao;
    private Currency usd;
    private Long id;
    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;
    private Product product5;
    private String query;

    @Before
    public void setup() throws ProductNotFoundException {
        productDao = ArrayListProductDao.getInstance();
        usd = Currency.getInstance("USD");
        id = 100L;
        product1 = new Product(id, "test-1", "Samsung Galaxy S", null, usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        product2 = new Product(101L, "test-2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        product3 = new Product("test-3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg");
        product4 = new Product("test-4", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        product5 = new Product("test-5", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg");
        productDao.save(product1);
        productDao.save(product2);
        productDao.save(product3);
        productDao.save(product4);
        productDao.save(product5);
        query = "apple iphone";
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

    @Test(expected = ArgumentIsNullException.class)
    public void testGetProductNull() throws ProductNotFoundException {
        Long id = null;
        assertEquals(product1, productDao.getProduct(id));
        fail("Expected ProductNotFoundException");
    }

    @Test
    public void testFindProductsWithNullPrice() {
        assertFalse(productDao.findProducts(null, null, null).contains(product1));
    }

    @Test
    public void testFindProductsWithZeroStock() {
        assertFalse(productDao.findProducts(null, null, null).contains(product2));
    }

    @Test
    public void testFindProductsByQuery() {
        List<Product> products = new ArrayList<>(Arrays.asList(product4, product5));
        assertEquals(products, productDao.findProducts(query, null, null));
    }

    @Test
    public void testFindProductsSortDescriptionAsc() {
        List<Product> products = new ArrayList<>(Arrays.asList(product4, product5, product3));
        assertEquals(products, productDao.findProducts(null, SortField.description, SortOrder.asc));
    }

    @Test
    public void testFindProductsSortDescriptionDesc() {
        List<Product> products = new ArrayList<>(Arrays.asList(product3, product5, product4));
        assertEquals(products, productDao.findProducts(null, SortField.description, SortOrder.desc));
    }

    @Test
    public void testFindProductsSortPriceAsc() {
        List<Product> products = new ArrayList<>(Arrays.asList(product4, product3, product5));
        assertEquals(products, productDao.findProducts(null, SortField.price, SortOrder.asc));
    }

    @Test
    public void testFindProductsSortPriceDesc() {
        List<Product> products = new ArrayList<>(Arrays.asList(product5, product3, product4));
        assertEquals(products, productDao.findProducts(null, SortField.price, SortOrder.desc));
    }

    @Test
    public void testFindProductsSortDescriptionAscByQuery() {
        List<Product> products = new ArrayList<>(Arrays.asList(product4, product5));
        assertEquals(products, productDao.findProducts(query, SortField.description, SortOrder.asc));
    }

    @Test
    public void testFindProductsSortDescriptionDescByQuery() {
        List<Product> products = new ArrayList<>(Arrays.asList(product5, product4));
        assertEquals(products, productDao.findProducts(query, SortField.description, SortOrder.desc));
    }

    @Test
    public void testFindProductsSortPriceAscByQuery() {
        List<Product> products = new ArrayList<>(Arrays.asList(product4, product5));
        assertEquals(products, productDao.findProducts(query, SortField.price, SortOrder.asc));
    }

    @Test
    public void testFindProductsSortPriceDescByQuery() {
        List<Product> products = new ArrayList<>(Arrays.asList(product5, product4));
        assertEquals(products, productDao.findProducts(query, SortField.price, SortOrder.desc));
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

    @Test(expected = ArgumentIsNullException.class)
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

    @Test(expected = ArgumentIsNullException.class)
    public void testDeleteProductNull() throws ProductNotFoundException {
        Long id = null;
        productDao.delete(id);
        fail("Expected ProductNotFoundException");
    }

    @After
    public void destroy() throws ProductNotFoundException {
        productDao.resetInstance();
    }
}
