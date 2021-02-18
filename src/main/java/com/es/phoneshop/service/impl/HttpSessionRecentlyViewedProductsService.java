package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.ArgumentIsNullException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.RecentlyViewedProducts;
import com.es.phoneshop.service.RecentlyViewedProductsService;
import com.es.phoneshop.utils.RecentlyViewedProductsLoader;
import com.es.phoneshop.utils.impl.HttpSessionRecentlyViewedProductsLoader;

import java.util.LinkedList;
import java.util.List;

public class HttpSessionRecentlyViewedProductsService implements RecentlyViewedProductsService {
    private static HttpSessionRecentlyViewedProductsService instance;
    private final int AMOUNT_VIEWED_PRODUCTS = 3;
    private ProductDao productDao;

    private HttpSessionRecentlyViewedProductsService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static synchronized HttpSessionRecentlyViewedProductsService getInstance() {
        if (instance == null) {
            instance = new HttpSessionRecentlyViewedProductsService();
        }
        return instance;
    }

    @Override
    public void addToList(RecentlyViewedProductsLoader loader, Long productId) {
        if (productId == null) {
            throw new ArgumentIsNullException();
        }
        synchronized (HttpSessionRecentlyViewedProductsLoader.class) {
            RecentlyViewedProducts products = loader.getViewedProducts();
            products.getProductsList().remove(productId);
            if (products.getProductsList().size() == AMOUNT_VIEWED_PRODUCTS) {
                products.getProductsList().removeLast();
            }
            products.getProductsList().addFirst(productId);
            List<Product> result = new LinkedList<>();
            products.getProductsList().forEach(p -> result.add(productDao.getProduct(p)));
            loader.saveToRequest(result);
        }
    }
}
