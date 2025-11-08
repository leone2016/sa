package edu.miu.cs.cs425.stockcommandservice.domain;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "stock_events")
public class StockEvent {

    @Id
    private String id;

    private String productNumber;

    private StockEventType type;

    private Integer quantity;

    private Instant occurredOn;

    private long version;

    public StockEvent() {
    }

    private StockEvent(String productNumber, StockEventType type, Integer quantity, long version) {
        this.productNumber = productNumber;
        this.type = type;
        this.quantity = quantity;
        this.version = version;
        this.occurredOn = Instant.now();
    }

    public static StockEvent created(String productNumber, int quantity, long version) {
        return new StockEvent(productNumber, StockEventType.CREATED, quantity, version);
    }

    public static StockEvent updated(String productNumber, int quantity, long version) {
        return new StockEvent(productNumber, StockEventType.UPDATED, quantity, version);
    }

    public static StockEvent deleted(String productNumber, long version) {
        return new StockEvent(productNumber, StockEventType.DELETED, null, version);
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

    public StockEventType getType() {
        return type;
    }

    public void setType(StockEventType type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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


