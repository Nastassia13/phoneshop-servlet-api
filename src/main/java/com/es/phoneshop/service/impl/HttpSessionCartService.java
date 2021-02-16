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
            for (CartItem item : cart.getItems()) {
                if (item.getProduct().getId().equals(productId)) {
                    int resultQuantity = item.getQuantity() + quantity;
                    int index = cart.getItems().indexOf(item);
                    int quantityInCart = cart.getItems().get(index).getQuantity();
                    boolean isZero = checkStock(product, quantity, quantityInCart);
                    if (!isZero) {
                        cart.getItems().get(index).setQuantity(resultQuantity);
                    } else {
                        cart.getItems().remove(index);
                    }
                    return;
                }
            }
            checkStock(product, quantity, 0);
            cart.getItems().add(new CartItem(product, quantity));
        }
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
