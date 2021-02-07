package com.es.phoneshop.model.product;

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
    public Product getProduct(Long id) throws ProductNotFoundException {
        if (id == null) {
            throw new ProductNotFoundException(id);
        }
        synchronized (lock) {
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException(id));
        }
    }

    private int compare(Product p1, Product p2, SortField sortField, SortOrder sortOrder) {
        int compareDiff = 0;
        if (sortField == SortField.price) {
            compareDiff = p1.getPrice().compareTo(p2.getPrice());
        } else if (sortField == SortField.description) {
            compareDiff = p1.getDescription().compareTo(p2.getDescription());
        }
        return SortOrder.desc == sortOrder ? -compareDiff : compareDiff;
    }

    private List<Product> findByQuery(String query) {
        String[] words = query.split("\\s");
        synchronized (lock) {
            return products.stream()
                    .collect(Collectors.toMap(product -> product,
                            product -> Arrays.stream(words)
                                    .filter(word -> product
                                            .getDescription()
                                            .toLowerCase()
                                            .contains(word.toLowerCase()))
                                    .count()))
                    .entrySet().stream()
                    .filter(entry -> entry.getValue() != 0)
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        List<Product> queryProducts = query == null || query.isEmpty() ? products : findByQuery(query);
        synchronized (lock) {
            return queryProducts.stream()
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .sorted((p1, p2) -> compare(p1, p2, sortField, sortOrder))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void save(Product product) throws ProductNotFoundException {
        if (product == null) {
            throw new ProductNotFoundException();
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
    public void delete(Long id) throws ProductNotFoundException {
        if (id == null) {
            throw new ProductNotFoundException(id);
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
