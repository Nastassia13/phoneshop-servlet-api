package com.es.phoneshop.service;

import com.es.phoneshop.exception.ParseToIntegerException;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.ParseException;

public class ParserService {
    private static ParserService instance;

    private ParserService() {
    }

    public static ParserService getInstance() {
        if (instance == null) {
            instance = new ParserService();
        }
        return instance;
    }

    public Long parseProductId(HttpServletRequest request) {
        String productId = request.getPathInfo().substring(1);
        return Long.valueOf(productId);
    }

    public int parseQuantity(String quantityString, HttpServletRequest request) throws ParseException, ParseToIntegerException {
        int quantity;
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        Number parsed = format.parse(quantityString);
        if (parsed.doubleValue() - parsed.intValue() != 0) {
            throw new ParseToIntegerException();
        }
        quantity = format.parse(quantityString).intValue();
        return quantity;
    }
}
