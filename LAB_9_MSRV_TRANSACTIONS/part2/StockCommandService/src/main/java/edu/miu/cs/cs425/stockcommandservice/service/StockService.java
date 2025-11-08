package edu.miu.cs.cs425.stockcommandservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.miu.cs.cs425.stockcommandservice.domain.Stock;
import edu.miu.cs.cs425.stockcommandservice.repository.StockRepository;

@Service
@Transactional
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Stock create(Stock stock) {
        if (stockRepository.existsById(stock.getProductNumber())) {
            throw new IllegalArgumentException("Stock for product %s already exists".formatted(stock.getProductNumber()));
        }
        return stockRepository.save(stock);
    }

    public Stock update(String productNumber, int quantity) {
        Stock existing = stockRepository.findById(productNumber)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found for product: " + productNumber));
        existing.setQuantity(quantity);
        return stockRepository.save(existing);
    }

    public void delete(String productNumber) {
        if (!stockRepository.existsById(productNumber)) {
            throw new IllegalArgumentException("Stock not found for product: " + productNumber);
        }
        stockRepository.deleteById(productNumber);
    }

    public List<Stock> findAll() {
        return stockRepository.findAll();
    }
}

