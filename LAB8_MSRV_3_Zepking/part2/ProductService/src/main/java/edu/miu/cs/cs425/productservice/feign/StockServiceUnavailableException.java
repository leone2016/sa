package edu.miu.cs.cs425.productservice.feign;

public class StockServiceUnavailableException extends RuntimeException {

    public StockServiceUnavailableException(String message) {
        super(message);
    }

    public StockServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}


