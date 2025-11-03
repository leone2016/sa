package edu.miu.cs.cs425.stockservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/stock")
public class StockController {
    StockRepository stockRepository = new StockRepository();

    @GetMapping("/{productNumber}")
    public ResponseEntity<Integer> getStockByProductNumber(@PathVariable String productNumber) {
       Optional<Stock> stock = stockRepository.getStocks()
                .stream()
                .filter(s -> s.getProductNumber().equals(productNumber))
                .findFirst();

         if(stock.isPresent()){
                return ResponseEntity.ok(stock.get().getQuantity());
         }
        return ResponseEntity.notFound().build();
    }
}
