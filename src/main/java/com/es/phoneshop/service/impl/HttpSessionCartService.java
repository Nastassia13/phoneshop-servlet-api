package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;

import javax.servlet.http.HttpServletRequest;

public class HttpSessionCartService implements CartService {
    private static HttpSessionCartService instance;
    private static final String CART_SESSION_ATTRIBUTE = HttpSessionCartService.class.getName() + ".cart";
    private ProductDao productDao;

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
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            cart = new Cart();
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart);
        }
        return cart;
    }

    @Override
    public synchronized void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                int resultQuantity = item.getQuantity() + quantity;
                int index = cart.getItems().indexOf(item);
                checkStock(product, quantity, cart.getItems().get(index).getQuantity());
                cart.getItems().get(index).setQuantity(resultQuantity);
                return;
            }
        }
        checkStock(product, quantity, 0);
        cart.getItems().add(new CartItem(product, quantity));
    }

    private void checkStock(Product product, int quantity, int quantityInCart) throws OutOfStockException {
        if (product.getStock() < quantity + quantityInCart) {
            throw new OutOfStockException(product, quantity, product.getStock() - quantityInCart);
        }
    }
}
