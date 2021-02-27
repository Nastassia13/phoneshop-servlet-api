package com.es.phoneshop.dao;

import com.es.phoneshop.model.order.Order;

public interface OrderDao extends Dao<Order>{
    Order getOrderBySecureId(String secureId);
}
