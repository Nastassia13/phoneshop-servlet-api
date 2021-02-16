package com.es.phoneshop.service;

import com.es.phoneshop.utils.RecentlyViewedProductsLoader;

public interface RecentlyViewedProductsService {
    void addToList(RecentlyViewedProductsLoader loader, Long productId);
}
