package edu.miu.cs.cs425.productservice.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StockServiceFeignConfig {

    private static final Logger logger = LoggerFactory.getLogger(StockServiceFeignConfig.class);

    @Bean
    public ErrorDecoder stockServiceErrorDecoder() {
        return new ErrorDecoder() {
            private final ErrorDecoder defaultDecoder = new Default();

            @Override
            public Exception decode(String methodKey, Response response) {
                // Treat 503 or LoadBalancer errors as StockServiceUnavailableException to trigger fallback
                if (response.status() == 503 || containsLoadBalancerError(response)) {
                    String message = String.format("StockService unavailable (status %d) for method %s", response.status(), methodKey);
                    logger.warn("Intercepted LoadBalancer/503 error. Mapping to StockServiceUnavailableException: {}", message);
                    return new StockServiceUnavailableException(message);
                }
                return defaultDecoder.decode(methodKey, response);
            }

            private boolean containsLoadBalancerError(Response response) {
                if (response.reason() != null && response.reason().toLowerCase().contains("load balancer")) {
                    return true;
                }
                return false;
            }
        };
    }
}


