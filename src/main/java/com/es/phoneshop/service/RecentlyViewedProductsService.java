package com.es.phoneshop.service;

import javax.servlet.http.HttpServletRequest;

public interface RecentlyViewedProductsService {
    void addToList(HttpServletRequest request, Long productId);
}
