package edu.miu.cs.cs425.productcommandservice.domain;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product_events")
public class ProductEvent {

    @Id
    private String id;

    private String productNumber;

    private ProductEventType type;

    private String name;

    private BigDecimal price;

    private Instant occurredOn;

    private long version;

    public ProductEvent() {
    }

    private ProductEvent(String productNumber, ProductEventType type, String name, BigDecimal price, long version) {
        this.productNumber = productNumber;
        this.type = type;
        this.name = name;
        this.price = price;
        this.version = version;
        this.occurredOn = Instant.now();
    }

    public static ProductEvent created(String productNumber, String name, BigDecimal price, long version) {
        return new ProductEvent(productNumber, ProductEventType.CREATED, name, price, version);
    }

    public static ProductEvent updated(String productNumber, String name, BigDecimal price, long version) {
        return new ProductEvent(productNumber, ProductEventType.UPDATED, name, price, version);
    }

    public static ProductEvent deleted(String productNumber, long version) {
        return new ProductEvent(productNumber, ProductEventType.DELETED, null, null, version);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public ProductEventType getType() {
        return type;
    }

    public void setType(ProductEventType type) {
        this.type = type;
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

    public Instant getOccurredOn() {
        return occurredOn;
    }

    public void setOccurredOn(Instant occurredOn) {
        this.occurredOn = occurredOn;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}


