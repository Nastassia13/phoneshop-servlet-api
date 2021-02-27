package com.es.phoneshop.dao;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;

import java.util.List;

public interface ProductDao extends Dao<Product> {
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    void delete(Long id);
}
