package com.es.phoneshop.model.product;

import java.util.LinkedList;

public class RecentlyViewedProducts {
    LinkedList<Long> productsList;

    public RecentlyViewedProducts() {
        this.productsList = new LinkedList<>();
    }

    public RecentlyViewedProducts(LinkedList<Long> productsList) {
        this.productsList = productsList;
    }

    public LinkedList<Long> getProductsList() {
        return productsList;
    }
}
