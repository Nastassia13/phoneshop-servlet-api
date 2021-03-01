package com.es.phoneshop.web.listener;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.web.listener.ProductDemodataServletContextListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDemodataServletContextListenerTest {
    @Mock
    ServletContextEvent event;
    @Mock
    ServletContext context;
    boolean insert = true;

    @InjectMocks
    ProductDemodataServletContextListener contextListener = new ProductDemodataServletContextListener();

    @Before
    public void setup() {
        when(event.getServletContext()).thenReturn(context);
        when(context.getInitParameter("insertDemoData")).thenReturn(String.valueOf(insert));
    }

    @Test
    public void testContextInitialized() {
        contextListener.contextInitialized(event);
        assertFalse(ArrayListProductDao.getInstance().findProducts(null, null, null).isEmpty());
    }
}
