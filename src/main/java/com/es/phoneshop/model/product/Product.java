package com.es.phoneshop.model.product;

import com.es.phoneshop.model.item.Item;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class Product extends Item implements Serializable {
    private String code;
    private String description;
    /**
     * null means there is no price because the product is outdated or new
     */
    private BigDecimal price;
    /**
     * can be null if the price is null
     */
    private Currency currency;
    private int stock;
    private String imageUrl;
    private List<PriceHistory> history = new ArrayList<>();

    public Product() {
    }

    public Product(String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this.code = code;
        this.description = description;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
        appendPrice(price);
    }

    public Product(Long id, String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
        appendPrice(price);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<PriceHistory> getHistory() {
        return history;
    }

    public void setHistory(List<PriceHistory> history) {
        this.history = history;
    }

    public void appendPrice(BigDecimal price) {
        this.price = price;
        history.add(new PriceHistory(new Date(), price));
    }
}