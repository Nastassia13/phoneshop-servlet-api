package com.es.phoneshop.utils;

import com.es.phoneshop.exception.ParseToIntegerException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataParserTest {
    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private DataParser dataParser = new DataParser();

    @Before
    public void setup() {

    }

    @Test
    public void testParseProductId() {
        when(request.getPathInfo()).thenReturn("/123");
        Long productId = dataParser.parseProductId(request);
        assertEquals(Long.valueOf(123L), productId);
    }

    @Test
    public void testParseQuantity() throws ParseException, ParseToIntegerException {
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        String quantityString = "12";
        int quantity = dataParser.parseQuantity(quantityString, request);
        assertEquals(12, quantity);
    }

    @Test(expected = ParseToIntegerException.class)
    public void testParseQuantityWithDot() throws ParseException, ParseToIntegerException {
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        String quantityString = "1.2";
        int quantity = dataParser.parseQuantity(quantityString, request);
        fail("Expected ParseToIntegerException");
    }

    @Test
    public void testParseQuantityWithComma() throws ParseException, ParseToIntegerException {
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        String quantityString = "1,2";
        int quantity = dataParser.parseQuantity(quantityString, request);
        assertEquals(12, quantity);
    }

    @Test(expected = ParseException.class)
    public void testParseQuantityString() throws ParseException, ParseToIntegerException {
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        String quantityString = "asd";
        int quantity = dataParser.parseQuantity(quantityString, request);
        fail("Expected ParseException");
    }

    @Test
    public void testParseQuantityRu() throws ParseException, ParseToIntegerException {
        when(request.getLocale()).thenReturn(new Locale("ru"));
        String quantityString = "12";
        int quantity = dataParser.parseQuantity(quantityString, request);
        assertEquals(12, quantity);
    }

    @Test
    public void testParseQuantityWithDotRu() throws ParseException, ParseToIntegerException {
        when(request.getLocale()).thenReturn(new Locale("ru"));
        String quantityString = "1.2";
        int quantity = dataParser.parseQuantity(quantityString, request);
        assertEquals(1, quantity);
    }

    @Test(expected = ParseToIntegerException.class)
    public void testParseQuantityWithCommaRu() throws ParseException, ParseToIntegerException {
        when(request.getLocale()).thenReturn(new Locale("ru"));
        String quantityString = "1,2";
        int quantity = dataParser.parseQuantity(quantityString, request);
        assertEquals(12, quantity);
        fail("Expected ParseToIntegerException");
    }

    @Test(expected = ParseException.class)
    public void testParseQuantityStringRu() throws ParseException, ParseToIntegerException {
        when(request.getLocale()).thenReturn(new Locale("ru"));
        String quantityString = "asd";
        int quantity = dataParser.parseQuantity(quantityString, request);
        fail("Expected ParseException");
    }
}
