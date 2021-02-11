package com.es.phoneshop.model.product;

import java.util.LinkedList;

public class RecentlyViewedProducts {
    LinkedList<Product> productsList;

    public RecentlyViewedProducts() {
        this.productsList = new LinkedList<>();
    }

    public LinkedList<Product> getProductsList() {
        return productsList;
    }
}
