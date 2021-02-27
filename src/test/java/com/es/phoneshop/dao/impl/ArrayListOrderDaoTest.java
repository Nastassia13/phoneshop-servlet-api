package com.es.phoneshop.dao.impl;

import com.es.phoneshop.exception.ArgumentIsNullException;
import com.es.phoneshop.exception.NotFoundException;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListOrderDaoTest {
    private ArrayListOrderDao orderDao;
    private Long id = 1L;
    Order order1;
    Order order2;
    String secureId1;
    String secureId2;

    @Before
    public void setup() throws NotFoundException {
        orderDao = ArrayListOrderDao.getInstance();
        secureId1 = UUID.randomUUID().toString();
        secureId2 = UUID.randomUUID().toString();
        order1 = new Order();
        order1.setSecureId(secureId1);
        order2 = new Order();
        order2.setSecureId(secureId2);
        orderDao.save(order1);
        orderDao.save(order2);
    }

    @Test
    public void testGetOrder() throws NotFoundException {
        assertEquals(order1, orderDao.getItem(id));
    }

    @Test(expected = NotFoundException.class)
    public void testGetOrderNotFound() throws NotFoundException {
        Long id = 0L;
        orderDao.getItem(id);
        fail("Expected NotFoundException");
    }

    @Test(expected = ArgumentIsNullException.class)
    public void testGetOrderNull() throws NotFoundException {
        Long id = null;
        assertEquals(order1, orderDao.getItem(id));
        fail("Expected NotFoundException");
    }

    @Test
    public void testSaveOrder() throws NotFoundException {
        Order order = new Order();
        orderDao.save(order);
        Long id = order.getId();
        assertNotNull(id);
        assertEquals(order, orderDao.getItem(id));
    }

    @Test
    public void testSaveOrderWithId() throws NotFoundException {
        Long id = 10L;
        Order order = new Order();
        order.setId(id);
        orderDao.save(order);
        assertEquals(order, orderDao.getItem(id));
    }

    @Test
    public void testSaveOrderWithExistingId() throws NotFoundException {
        Order order = new Order();
        order.setId(1L);
        orderDao.save(order);
        assertEquals(order, orderDao.getItem(id));
    }

    @Test(expected = ArgumentIsNullException.class)
    public void testSaveOrderNull() throws NotFoundException {
        Order order = null;
        orderDao.save(order);
        fail("Expected ArgumentIsNullException");
    }

    @Test
    public void testGetOrderBySecureId() throws NotFoundException {
        assertEquals(order1, orderDao.getOrderBySecureId(secureId1));
    }

    @Test(expected = OrderNotFoundException.class)
    public void testGetOrderBySecureIdNotFound() throws NotFoundException {
        String secureId = UUID.randomUUID().toString();
        orderDao.getOrderBySecureId(secureId);
        fail("Expected OrderNotFoundException");
    }

    @Test(expected = ArgumentIsNullException.class)
    public void testGetOrderBySecureIdNull() throws NotFoundException {
        String secureId = null;
        assertEquals(order1, orderDao.getOrderBySecureId(secureId));
        fail("Expected ArgumentIsNullException");
    }

    @After
    public void destroy() throws NotFoundException {
        //orderDao.resetInstance();
    }
}
