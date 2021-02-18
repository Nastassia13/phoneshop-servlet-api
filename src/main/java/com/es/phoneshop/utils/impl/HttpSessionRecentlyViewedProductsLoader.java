package com.es.phoneshop.utils.impl;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.RecentlyViewedProducts;
import com.es.phoneshop.service.impl.HttpSessionRecentlyViewedProductsService;
import com.es.phoneshop.utils.RecentlyViewedProductsLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class HttpSessionRecentlyViewedProductsLoader implements RecentlyViewedProductsLoader {
    private static final String VIEWED_SESSION_ATTRIBUTE = HttpSessionRecentlyViewedProductsService.class.getName() + ".viewed";
    private HttpServletRequest request;
    private HttpSession session;

    public HttpSessionRecentlyViewedProductsLoader(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public RecentlyViewedProducts getViewedProducts() {
        session = request.getSession();
        RecentlyViewedProducts viewedProducts = (RecentlyViewedProducts) session.getAttribute(VIEWED_SESSION_ATTRIBUTE);
        if (viewedProducts == null) {
            viewedProducts = new RecentlyViewedProducts();
            session.setAttribute(VIEWED_SESSION_ATTRIBUTE, viewedProducts);
        }
        return viewedProducts;
    }

    @Override
    public void saveToRequest(List<Product> products) {
        session.setAttribute("viewedProducts", products);
    }
}
