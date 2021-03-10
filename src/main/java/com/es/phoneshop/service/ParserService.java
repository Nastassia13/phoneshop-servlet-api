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

    public int parseNumber(String numberString, HttpServletRequest request) throws ParseException, ParseToIntegerException {
        int number;
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        Number parsed = format.parse(numberString);
        if (parsed.doubleValue() - parsed.intValue() != 0) {
            throw new ParseToIntegerException();
        }
        number = format.parse(numberString).intValue();
        return number;
    }
}
