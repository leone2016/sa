package edu.miu.cs.cs425.consumer;

class OrderProcessingException extends RuntimeException {

    OrderProcessingException(String message) {
        super(message);
    }

    OrderProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

