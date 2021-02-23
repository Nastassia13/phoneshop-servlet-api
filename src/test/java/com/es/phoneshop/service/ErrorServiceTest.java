package com.es.phoneshop.service;

import com.es.phoneshop.exception.OutOfQuantityException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ParseToIntegerException;
import com.es.phoneshop.model.product.Product;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ErrorServiceTest {
    private Map<Long, String> errors = new HashMap<>();
    private ErrorService errorService = ErrorService.getInstance();

    @Test
    public void handleErrorParseException() {
        Exception e = new ParseException("Error", 1);
        String error = errorService.handleError(e);
        assertEquals("Not a number", error);
    }

    @Test
    public void handleErrorParseToIntegerException() {
        Exception e = new ParseToIntegerException();
        String error = errorService.handleError(e);
        assertEquals("Not a number", error);
    }

    @Test
    public void handleErrorOutOfStockException() {
        int available = 5;
        Exception e = new OutOfStockException(new Product(), 10, available);
        String error = errorService.handleError(e);
        assertEquals("Not enough stock. Available: " + available, error);
    }

    @Test
    public void handleErrorOutOfQuantityExceptionException() {
        Exception e = new OutOfQuantityException();
        String error = errorService.handleError(e);
        assertEquals("Insufficient quantity", error);
    }

    @Test
    public void handleErrorOtherException() {
        Exception e = new Exception();
        String error = errorService.handleError(e);
        assertEquals("Invalid input", error);
    }

    @Test
    public void handleErrorParseExceptionMap() {
        Long productId = 1L;
        Exception e = new ParseException("Error", 1);
        errorService.handleErrors(errors, productId, e);
        assertEquals("Not a number", errors.get(productId));
    }

    @Test
    public void handleErrorParseToIntegerExceptionMap() {
        Long productId = 2L;
        Exception e = new ParseToIntegerException();
        errorService.handleErrors(errors, productId, e);
        assertEquals("Not a number", errors.get(productId));
    }

    @Test
    public void handleErrorOutOfStockExceptionMap() {
        Long productId = 3L;
        int available = 5;
        Exception e = new OutOfStockException(new Product(), 10, available);
        errorService.handleErrors(errors, productId, e);
        assertEquals("Not enough stock. Available: " + available, errors.get(productId));
    }

    @Test
    public void handleErrorOutOfQuantityExceptionExceptionMap() {
        Long productId = 4L;
        Exception e = new OutOfQuantityException();
        errorService.handleErrors(errors, productId, e);
        assertEquals("Insufficient quantity", errors.get(productId));
    }
}
