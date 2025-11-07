package edu.miu.cs.cs425.productqueryservice.service;

import java.math.BigDecimal;

public class ProductView {

    private final String productNumber;
    private final String name;
    private final BigDecimal price;
    private final int numberInStock;

    public ProductView(String productNumber, String name, BigDecimal price, int numberInStock) {
        this.productNumber = productNumber;
        this.name = name;
        this.price = price;
        this.numberInStock = numberInStock;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getNumberInStock() {
        return numberInStock;
    }
}

