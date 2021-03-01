package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.ArgumentIsNullException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;

import java.util.*;
import java.util.stream.Collectors;

public class ArrayListProductDao extends AbstractGenericDao<Product> implements ProductDao {
    private static ArrayListProductDao instance;

    private ArrayListProductDao() {
    }

    public static synchronized ArrayListProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    private void calculateRating(Product product, String query, Map<Product, Double> rating) {
        if (query == null || query.isEmpty()) {
            rating.put(product, 1d);
            return;
        }
        String[] words = query.split("\\s");
        int wordsInDescription = product.getDescription().split("\\s").length;
        long count = Arrays.stream(words)
                .filter(word -> product.getDescription().toLowerCase().contains(word.toLowerCase()))
                .count();
        double relativeCount = (double) count / wordsInDescription;
        rating.put(product, relativeCount);
    }

    private Comparator<Map.Entry<Product, Double>> defineComparator(String query, SortField sortField, SortOrder sortOrder) {
        Comparator<Map.Entry<Product, Double>> comparator = (o1, o2) -> 0;
        if (query != null && !query.isEmpty()) {
            comparator = Map.Entry.comparingByValue(Comparator.reverseOrder());
        }
        if (sortField == SortField.price) {
            comparator = Map.Entry.comparingByKey(Comparator.comparing(Product::getPrice));
        } else if (sortField == SortField.description) {
            comparator = Map.Entry.comparingByKey(Comparator.comparing(Product::getDescription));
        }
        if (sortOrder == SortOrder.desc) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        synchronized (lock) {
            Map<Product, Double> rating = new HashMap<>();
            Comparator<Map.Entry<Product, Double>> comparator = defineComparator(query, sortField, sortOrder);
            items.forEach(product -> calculateRating(product, query, rating));
            return rating.entrySet().stream()
                    .filter(entry -> entry.getValue() != 0)
                    .filter(entry -> entry.getKey().getPrice() != null)
                    .filter(entry -> entry.getKey().getStock() > 0)
                    .sorted(comparator)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new ArgumentIsNullException();
        }
        synchronized (lock) {
            items.stream()
                    .filter(p -> id.equals(p.getId()))
                    .findAny()
                    .ifPresent(items::remove);
        }
    }

    public void resetInstance() {
        instance = null;
    }
}
