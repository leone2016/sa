package edu.miu.cs.cs425.productcommandservice.domain;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class Product {

    @Id
    private String productNumber;

    private String name;

    private BigDecimal price;

    public Product() {
    }

    public Product(String productNumber, String name, BigDecimal price) {
        this.productNumber = productNumber;
        this.name = name;
        this.price = price;
    }

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

