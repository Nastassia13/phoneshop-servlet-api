package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.ArgumentIsNullException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.TypeSearch;
import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ArrayListProductDao extends AbstractGenericDao<Product> implements ProductDao {
    private static ArrayListProductDao instance;
    private final BigDecimal MAX_COST = BigDecimal.valueOf(1000000000000L);

    private ArrayListProductDao() {
    }

    public static synchronized ArrayListProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    private void calculateRatingAndSave(Product product, String query, Map<Product, Double> rating) {
        if (query == null || query.isEmpty()) {
            rating.put(product, 1d);
            return;
        }
        double relativeCount = calculateRating(product, query);
        rating.put(product, relativeCount);
    }

    private double calculateRating(Product product, String query) {
        long count = calculateNumberCoincidences(product, query);
        int wordsInDescription = product.getDescription().split("\\s").length;
        return (double) count / wordsInDescription;
    }

    private long calculateNumberCoincidences(Product product, String query) {
        String[] words = query.split("\\s");
        return Arrays.stream(words)
                .filter(word -> product.getDescription().toLowerCase().contains(word.toLowerCase()))
                .count();
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
            items.forEach(product -> calculateRatingAndSave(product, query, rating));
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
    public List<Product> findSearchAdvancedProducts(String description, String typeSearch, BigDecimal minPrice, BigDecimal maxPrice) {
        if ((description == null || description.isEmpty()) && (typeSearch == null || typeSearch.isEmpty()) && minPrice == null && maxPrice == null) {
            return items;
        } else if (description == null) {
            description = "";
        } else if (typeSearch == null || typeSearch.isEmpty()) {
            typeSearch = TypeSearch.ALL_WORDS.toString();
        }
        synchronized (lock) {
            String finalDescription = description;
            String finalTypeSearch = typeSearch;
            return items.stream()
                    .filter(item -> isFitByDescription(item, finalDescription, TypeSearch.valueOf(finalTypeSearch)))
                    .filter(item -> checkPrice(minPrice, false).compareTo(item.getPrice()) <= 0)
                    .filter(item -> checkPrice(maxPrice, true).compareTo(item.getPrice()) >= 0)
                    .collect(Collectors.toList());
        }
    }

    private BigDecimal checkPrice(BigDecimal price, boolean isMax) {
        if (price == null) {
            if (!isMax) {
                price = BigDecimal.ZERO;
            } else {
                price = MAX_COST;
            }
        }
        return price;
    }

    private boolean isFitByDescription(Product product, String description, TypeSearch typeSearch) {
        if (typeSearch == TypeSearch.ALL_WORDS) {
            long countCoincidences = calculateNumberCoincidences(product, description);
            int countWords = description.split("\\s").length;
            return countCoincidences == countWords;
        } else {
            return calculateRating(product, description) != 0;
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
