package edu.miu.cs.cs425.productservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "stock-service")
public interface StockServiceClient {
    @GetMapping("/stock/{productNumber}")
    Integer getStockByProductNumber(@PathVariable("productNumber") String productNumber);
}

