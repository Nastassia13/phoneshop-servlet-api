package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.ArgumentIsNullException;
import com.es.phoneshop.exception.OutOfQuantityException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.utils.CartLoader;

import java.math.BigDecimal;
import java.util.Optional;

public class HttpSessionCartService implements CartService {
    private static HttpSessionCartService instance;
    private ProductDao productDao;
    private final Object cartLock = new Object();

    private HttpSessionCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static synchronized HttpSessionCartService getInstance() {
        if (instance == null) {
            instance = new HttpSessionCartService();
        }
        return instance;
    }

    @Override
    public Cart getCart(CartLoader cartLoader) {
        if (cartLoader == null) {
            throw new ArgumentIsNullException();
        }
        return cartLoader.getCart();
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException, OutOfQuantityException {
        if (cart == null || productId == null) {
            throw new ArgumentIsNullException();
        }
        synchronized (cartLock) {
            Product product = productDao.getItem(productId);
            Optional<CartItem> cartItem = findCartItem(cart, productId);
            int quantityInCart = cartItem.map(CartItem::getQuantity).orElse(0);
            boolean isZero = checkStock(product, quantity, quantityInCart);
            cartItem.ifPresentOrElse(item -> changeCart(cart, item, isZero, quantity + quantityInCart),
                    () -> {
                        if (!isZero) {
                            cart.getItems().add(new CartItem(product, quantity));
                        }
                    });
            recalculateCart(cart);
        }
    }

    @Override
    public void update(Cart cart, Long productId, int quantity) throws OutOfStockException, OutOfQuantityException {
        if (cart == null || productId == null) {
            throw new ArgumentIsNullException();
        }
        synchronized (cartLock) {
            Product product = productDao.getItem(productId);
            Optional<CartItem> cartItem = findCartItem(cart, productId);
            boolean isZero = checkStock(product, quantity, 0);
            cartItem.ifPresent(item -> changeCart(cart, item, isZero, quantity));
            recalculateCart(cart);
        }
    }

    @Override
    public void delete(Cart cart, Long productId) {
        if (cart == null || productId == null) {
            throw new ArgumentIsNullException();
        }
        synchronized (cartLock) {
            cart.getItems().removeIf(item -> productId.equals(item.getProduct().getId()));
            recalculateCart(cart);
        }
    }

    private void recalculateCart(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum());
        cart.setTotalCost(cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private Optional<CartItem> findCartItem(Cart cart, Long productId) {
        return cart.getItems().stream()
                .filter(item -> productId.equals(item.getProduct().getId()))
                .findAny();
    }

    private boolean checkStock(Product product, int quantity, int quantityInCart) throws OutOfStockException, OutOfQuantityException {
        if (product.getStock() < quantity + quantityInCart) {
            throw new OutOfStockException(product, quantity, product.getStock() - quantityInCart);
        }
        if (quantity < 0 && quantityInCart < Math.abs(quantity)) {
            throw new OutOfQuantityException();
        }
        return quantity + quantityInCart == 0;
    }

    private void changeCart(Cart cart, CartItem item, boolean isZero, int quantity) {
        if (!isZero) {
            item.setQuantity(quantity);
        } else {
            cart.getItems().remove(item);
        }
    }

    @Override
    public void clearCart(Cart cart) {
        cart.getItems().removeAll(cart.getItems());
        recalculateCart(cart);
    }
}
