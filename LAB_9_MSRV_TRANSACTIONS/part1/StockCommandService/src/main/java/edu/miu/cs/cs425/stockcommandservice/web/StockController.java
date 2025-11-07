package edu.miu.cs.cs425.stockcommandservice.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.miu.cs.cs425.stockcommandservice.domain.Stock;
import edu.miu.cs.cs425.stockcommandservice.service.StockService;

@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<Stock> create(@RequestBody StockRequest request) {
        Stock created = stockService.create(new Stock(request.getProductNumber(), request.getQuantity()));
        return ResponseEntity.created(URI.create("/stocks/" + created.getProductNumber())).body(created);
    }

    @PutMapping("/{productNumber}")
    public ResponseEntity<Stock> update(@PathVariable String productNumber,
                                        @RequestBody StockRequest request) {
        Stock updated = stockService.update(productNumber, request.getQuantity());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{productNumber}")
    public ResponseEntity<Void> delete(@PathVariable String productNumber) {
        stockService.delete(productNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Stock> findAll() {
        return stockService.findAll();
    }
}

