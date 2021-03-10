package com.es.phoneshop.dao;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.TypeSearch;
import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao extends Dao<Product> {
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    List<Product> findSearchAdvancedProducts(String description, String typeSearch, BigDecimal minPrice, BigDecimal maxPrice);

    void delete(Long id);
}
