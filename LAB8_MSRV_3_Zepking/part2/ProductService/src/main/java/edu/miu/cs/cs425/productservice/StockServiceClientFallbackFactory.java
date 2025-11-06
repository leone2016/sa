package edu.miu.cs.cs425.productservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class StockServiceClientFallbackFactory implements FallbackFactory<StockServiceClient> {

    private static final Logger logger = LoggerFactory.getLogger(StockServiceClientFallbackFactory.class);

    @Override
    public StockServiceClient create(Throwable cause) {
        return productNumber -> {
            String reason = cause != null ? cause.getMessage() : "unknown";
            logger.warn("⚠️ Fallback triggered for product {}. Returning default stock. Cause: {}", productNumber, reason, cause);
            return 0; // default stock value when StockService is unavailable
        };
    }
}
