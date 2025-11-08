package edu.miu.cs.cs425.productcommandservice.web;

import java.math.BigDecimal;

public class ProductRequest {

    private String productNumber;

    private String name;

    private BigDecimal price;

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

