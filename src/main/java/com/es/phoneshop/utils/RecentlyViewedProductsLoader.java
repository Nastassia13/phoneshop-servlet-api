package com.es.phoneshop.utils;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.RecentlyViewedProducts;

import java.util.List;

public interface RecentlyViewedProductsLoader {
    RecentlyViewedProducts getViewedProducts();
    void saveToRequest(List<Product> products);
}
