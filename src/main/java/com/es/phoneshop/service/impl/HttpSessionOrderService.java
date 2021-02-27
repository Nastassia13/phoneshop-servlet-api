package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.exception.ArgumentIsNullException;
import com.es.phoneshop.exception.CloneCartException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.OrderService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class HttpSessionOrderService implements OrderService {
    private static HttpSessionOrderService instance;
    private BigDecimal DELIVERY_COST = BigDecimal.valueOf(5);
    private OrderDao orderDao;

    private HttpSessionOrderService() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    public static synchronized HttpSessionOrderService getInstance() {
        if (instance == null) {
            instance = new HttpSessionOrderService();
        }
        return instance;
    }

    @Override
    public Order getOrder(Cart cart) {
        if (cart == null) {
            throw new ArgumentIsNullException();
        }
        Order order = new Order();
        order.setItems(cart.getItems().stream().map(item -> {
            try {
                return (CartItem) item.clone();
            } catch (CloneNotSupportedException e) {
                throw new CloneCartException(e);
            }
        }).collect(Collectors.toList()));
        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalCost(order.getSubtotal().add(order.getDeliveryCost()));
        return order;
    }

    private BigDecimal calculateDeliveryCost() {
        return DELIVERY_COST;
    }

    @Override
    public List<PaymentMethod> getPaymentMethod() {
        return Arrays.asList(PaymentMethod.values());
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }
}
