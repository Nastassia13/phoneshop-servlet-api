package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.ArgumentIsNullException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.sortenum.SortField;
import com.es.phoneshop.model.sortenum.SortOrder;

import java.util.*;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private static ArrayListProductDao instance;
    private Long maxId;
    private List<Product> products;
    private final Object lock = new Object();

    private ArrayListProductDao() {
        products = new ArrayList<>();
        maxId = 0L;
    }

    public static synchronized ArrayListProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    @Override
    public Product getProduct(Long id) {
        if (id == null) {
            throw new ArgumentIsNullException();
        }
        synchronized (lock) {
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException(id));
        }
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
            products.forEach(product -> calculateRating(product, query, rating));
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
    public void save(Product product) {
        if (product == null) {
            throw new ArgumentIsNullException();
        }
        synchronized (lock) {
            Long id = product.getId();
            if (id != null) {
                Optional<Product> current = products.stream()
                        .filter(p -> id.equals(p.getId()))
                        .findAny();
                if (current.isPresent()) {
                    products.set(products.indexOf(current.get()), product);
                } else {
                    products.add(product);
                }
            } else {
                product.setId(++maxId);
                products.add(product);
            }
        }
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new ArgumentIsNullException();
        }
        synchronized (lock) {
            products.stream()
                    .filter(p -> id.equals(p.getId()))
                    .findAny()
                    .ifPresent(products::remove);
        }
    }

    public void resetInstance() {
        instance = null;
    }
}
