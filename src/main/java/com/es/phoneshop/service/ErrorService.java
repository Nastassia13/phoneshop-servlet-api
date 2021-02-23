package com.es.phoneshop.service;

import com.es.phoneshop.exception.OutOfStockException;

import java.util.Map;

public class ErrorService {
    private static ErrorService instance;
    private static final String PARSE_EXCEPTION = "java.text.ParseException";
    private static final String PARSE_TO_INT_EXCEPTION = "com.es.phoneshop.exception.ParseToIntegerException";
    private static final String STOCK_EXCEPTION = "com.es.phoneshop.exception.OutOfQuantityException";
    private static final String QUANTITY_EXCEPTION = "com.es.phoneshop.exception.OutOfStockException";

    private ErrorService() {
    }

    public static ErrorService getInstance() {
        if (instance == null) {
            instance = new ErrorService();
        }
        return instance;
    }

    public String handleError(Exception e) {
        switch (e.getClass().getName()) {
            case PARSE_EXCEPTION:
            case PARSE_TO_INT_EXCEPTION:
                return "Not a number";
            case STOCK_EXCEPTION:
                return "Not enough stock. Available: " + ((OutOfStockException) e).getStockAvailable();
            case QUANTITY_EXCEPTION:
                return "Insufficient quantity";
        }
        return "Invalid input";
    }

    public void handleErrors(Map<Long, String> errors, Long productId, Exception e) {
        switch (e.getClass().getName()) {
            case PARSE_EXCEPTION:
            case PARSE_TO_INT_EXCEPTION:
                errors.put(productId, "Not a number");
                break;
            case STOCK_EXCEPTION:
                errors.put(productId, "Not enough stock. Available: " + ((OutOfStockException) e).getStockAvailable());
                break;
            case QUANTITY_EXCEPTION:
                errors.put(productId, "Insufficient quantity");
                break;
        }
    }
}
