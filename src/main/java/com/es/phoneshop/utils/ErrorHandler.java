package com.es.phoneshop.utils;

import com.es.phoneshop.exception.OutOfStockException;

import java.util.Map;

public class ErrorHandler {
    public static String handleError(Exception e) {
        switch (e.getClass().getName()) {
            case "java.text.ParseException":
            case "com.es.phoneshop.exception.ParseToIntegerException":
                return "Not a number";
            case "com.es.phoneshop.exception.OutOfStockException":
                return "Not enough stock. Available: " + ((OutOfStockException) e).getStockAvailable();
            case "com.es.phoneshop.exception.OutOfQuantityException":
                return "Insufficient quantity";
        }
        return "Invalid input";
    }

    public static void handleErrors(Map<Long, String> errors, Long productId, Exception e) {
        switch (e.getClass().getName()) {
            case "java.text.ParseException":
            case "com.es.phoneshop.exception.ParseToIntegerException":
                errors.put(productId, "Not a number");
                break;
            case "com.es.phoneshop.exception.OutOfStockException":
                errors.put(productId, "Not enough stock. Available: " + ((OutOfStockException) e).getStockAvailable());
                break;
            case "com.es.phoneshop.exception.OutOfQuantityException":
                errors.put(productId, "Insufficient quantity");
                break;
        }
    }
}
