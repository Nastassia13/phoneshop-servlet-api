package com.es.phoneshop.utils;

import com.es.phoneshop.exception.OutOfQuantityException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ParseToIntegerException;
import com.es.phoneshop.model.product.Product;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ErrorHandlerTest {
    private Map<Long, String> errors = new HashMap<>();

    @Test
    public void handleErrorParseException() {
        Exception e = new ParseException("Error", 1);
        String error = ErrorHandler.handleError(e);
        assertEquals("Not a number", error);
    }

    @Test
    public void handleErrorParseToIntegerException() {
        Exception e = new ParseToIntegerException();
        String error = ErrorHandler.handleError(e);
        assertEquals("Not a number", error);
    }

    @Test
    public void handleErrorOutOfStockException() {
        int available = 5;
        Exception e = new OutOfStockException(new Product(), 10, available);
        String error = ErrorHandler.handleError(e);
        assertEquals("Not enough stock. Available: " + available, error);
    }

    @Test
    public void handleErrorOutOfQuantityExceptionException() {
        Exception e = new OutOfQuantityException();
        String error = ErrorHandler.handleError(e);
        assertEquals("Insufficient quantity", error);
    }

    @Test
    public void handleErrorOtherException() {
        Exception e = new Exception();
        String error = ErrorHandler.handleError(e);
        assertEquals("Invalid input", error);
    }

    @Test
    public void handleErrorParseExceptionMap() {
        Long productId = 1L;
        Exception e = new ParseException("Error", 1);
        ErrorHandler.handleErrors(errors, productId, e);
        assertEquals("Not a number", errors.get(productId));
    }

    @Test
    public void handleErrorParseToIntegerExceptionMap() {
        Long productId = 2L;
        Exception e = new ParseToIntegerException();
        ErrorHandler.handleErrors(errors, productId, e);
        assertEquals("Not a number", errors.get(productId));
    }

    @Test
    public void handleErrorOutOfStockExceptionMap() {
        Long productId = 3L;
        int available = 5;
        Exception e = new OutOfStockException(new Product(), 10, available);
        ErrorHandler.handleErrors(errors, productId, e);
        assertEquals("Not enough stock. Available: " + available, errors.get(productId));
    }

    @Test
    public void handleErrorOutOfQuantityExceptionExceptionMap() {
        Long productId = 4L;
        Exception e = new OutOfQuantityException();
        ErrorHandler.handleErrors(errors, productId, e);
        assertEquals("Insufficient quantity", errors.get(productId));
    }
}
