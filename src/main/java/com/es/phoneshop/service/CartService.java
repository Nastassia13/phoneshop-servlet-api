package com.es.phoneshop.service;

import com.es.phoneshop.exception.OutOfQuantityException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.utils.CartLoader;

public interface CartService {
    Cart getCart(CartLoader cartLoader);

    void add(Cart cart, Long productId, int quantity) throws OutOfStockException, OutOfQuantityException;

    void update(Cart cart, Long productId, int quantity) throws OutOfStockException, OutOfQuantityException;

    void delete(Cart cart, Long productId);
}
