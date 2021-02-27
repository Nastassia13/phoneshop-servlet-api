package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.ArgumentIsNullException;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

import java.util.ArrayList;

public class ArrayListOrderDao extends GenericDao<Order> implements OrderDao {
    private static ArrayListOrderDao instance;

    private ArrayListOrderDao() {
        items = new ArrayList<>();
    }

    public static synchronized ArrayListOrderDao getInstance() {
        if (instance == null) {
            instance = new ArrayListOrderDao();
        }
        return instance;
    }

    @Override
    public Order getOrderBySecureId(String secureId){
        if (secureId == null) {
            throw new ArgumentIsNullException();
        }
        synchronized (lock) {
            return items.stream()
                    .filter(o -> secureId.equals(o.getSecureId()))
                    .findAny()
                    .orElseThrow(OrderNotFoundException::new);
        }
    }
}
