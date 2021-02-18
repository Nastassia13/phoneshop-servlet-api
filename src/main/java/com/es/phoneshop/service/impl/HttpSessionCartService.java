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
    private final Object obj = new Object();

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
        synchronized (CartLoader.class) {
            return cartLoader.getCart();
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException, OutOfQuantityException {
        if (cart == null || productId == null) {
            throw new ArgumentIsNullException();
        }
        synchronized (obj) {
            Product product = productDao.getProduct(productId);
            Optional<CartItem> cartItem = findCartItem(cart, productId);
            boolean isZero;
            if (cartItem.isPresent()) {
                CartItem item = cartItem.get();
                int index = cart.getItems().indexOf(item);
                int quantityInCart = cart.getItems().get(index).getQuantity();
                isZero = checkStock(product, quantity, quantityInCart);
                int resultQuantity = item.getQuantity() + quantity;
                if (!isZero) {
                    cart.getItems().get(index).setQuantity(resultQuantity);
                } else {
                    cart.getItems().remove(index);
                }
            } else {
                isZero = checkStock(product, quantity, 0);
                if (!isZero) {
                    cart.getItems().add(new CartItem(product, quantity));
                } else {
                    throw new OutOfQuantityException();
                }
            }
            recalculateCart(cart);
        }
    }

    @Override
    public void update(Cart cart, Long productId, int quantity) throws OutOfStockException, OutOfQuantityException {
        if (cart == null || productId == null) {
            throw new ArgumentIsNullException();
        }
        synchronized (obj) {
            Product product = productDao.getProduct(productId);
            Optional<CartItem> cartItem = findCartItem(cart, productId);
            if (cartItem.isPresent()) {
                int index = cart.getItems().indexOf(cartItem.get());
                boolean isZero = checkStock(product, quantity, 0);
                if (!isZero) {
                    cart.getItems().get(index).setQuantity(quantity);
                } else {
                    throw new OutOfQuantityException();
                }
            }
            recalculateCart(cart);
        }
    }

    @Override
    public void delete(Cart cart, Long productId) {
        if (cart == null || productId == null) {
            throw new ArgumentIsNullException();
        }
        synchronized (obj) {
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
}
