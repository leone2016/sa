package edu.miu.cs.cs425.productservice;

import edu.miu.cs.cs425.productservice.feign.StockServiceFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "stock-service",
        fallbackFactory = StockServiceClientFallbackFactory.class,
        configuration = StockServiceFeignConfig.class
)
public interface StockServiceClient {
    @GetMapping("/stock/{productNumber}")
    Integer getStockByProductNumber(@PathVariable("productNumber") String productNumber);
}