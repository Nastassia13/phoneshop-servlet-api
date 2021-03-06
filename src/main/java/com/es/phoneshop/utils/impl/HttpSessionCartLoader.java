package com.es.phoneshop.utils.impl;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.impl.HttpSessionCartService;
import com.es.phoneshop.utils.CartLoader;

import javax.servlet.http.HttpServletRequest;

public class HttpSessionCartLoader implements CartLoader {
    private static final String CART_SESSION_ATTRIBUTE = HttpSessionCartService.class.getName() + ".cart";
    private HttpServletRequest request;
    private final Object loadLock = new Object();

    public HttpSessionCartLoader(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Cart getCart() {
        synchronized (loadLock) {
            Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                cart = new Cart();
                request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart);
            }
            return cart;
        }
    }
}
