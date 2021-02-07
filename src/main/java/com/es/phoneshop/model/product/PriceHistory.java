package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.Date;

public class PriceHistory {
    private Date startDate;
    private BigDecimal price;

    public PriceHistory(Date startDate, BigDecimal price) {
        this.startDate = startDate;
        this.price = price;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
