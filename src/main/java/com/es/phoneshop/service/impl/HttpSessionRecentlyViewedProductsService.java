package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.RecentlyViewedProducts;
import com.es.phoneshop.service.RecentlyViewedProductsService;

import javax.servlet.http.HttpServletRequest;

public class HttpSessionRecentlyViewedProductsService implements RecentlyViewedProductsService {
    private static HttpSessionRecentlyViewedProductsService instance;
    private static final String VIEWED_SESSION_ATTRIBUTE = HttpSessionRecentlyViewedProductsService.class.getName() + ".viewed";
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
    public synchronized void addToList(HttpServletRequest request, Long productId) {
        RecentlyViewedProducts viewedProducts = (RecentlyViewedProducts) request.getSession().getAttribute(VIEWED_SESSION_ATTRIBUTE);
        if (viewedProducts == null) {
            viewedProducts = new RecentlyViewedProducts();
            request.getSession().setAttribute(VIEWED_SESSION_ATTRIBUTE, viewedProducts);
        }
        Product product = productDao.getProduct(productId);
        viewedProducts.getProductsList().remove(product);
        if (viewedProducts.getProductsList().size() == 3) {
            viewedProducts.getProductsList().removeLast();
        }
        viewedProducts.getProductsList().addFirst(product);
        request.getSession().setAttribute("viewedProducts", viewedProducts.getProductsList());
    }
}
