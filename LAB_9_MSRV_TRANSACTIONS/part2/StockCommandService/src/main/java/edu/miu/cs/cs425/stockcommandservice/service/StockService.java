package edu.miu.cs.cs425.stockcommandservice.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.miu.cs.cs425.stockcommandservice.domain.Stock;
import edu.miu.cs.cs425.stockcommandservice.domain.StockEvent;
import edu.miu.cs.cs425.stockcommandservice.repository.StockEventRepository;
import edu.miu.cs.cs425.stockcommandservice.repository.StockRepository;

@Service
@Transactional
public class StockService {

    private final StockRepository stockRepository;
    private final StockEventRepository stockEventRepository;

    public StockService(StockRepository stockRepository,
                        StockEventRepository stockEventRepository) {
        this.stockRepository = stockRepository;
        this.stockEventRepository = stockEventRepository;
    }

    public Stock create(Stock stock) {
        List<StockEvent> history = loadHistory(stock.getProductNumber());
        if (replay(history).isPresent()) {
            throw new IllegalArgumentException("Stock for product %s already exists".formatted(stock.getProductNumber()));
        }

        StockEvent event = StockEvent.created(
                stock.getProductNumber(),
                stock.getQuantity(),
                nextVersion(history));
        stockEventRepository.save(event);

        return stockRepository.save(stock);
    }

    public Stock update(String productNumber, int quantity) {
        List<StockEvent> history = loadHistory(productNumber);
        Stock current = replay(history)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found for product: " + productNumber));

        current.setQuantity(quantity);

        StockEvent event = StockEvent.updated(
                productNumber,
                quantity,
                nextVersion(history));
        stockEventRepository.save(event);

        return stockRepository.save(current);
    }

    public void delete(String productNumber) {
        List<StockEvent> history = loadHistory(productNumber);
        Stock current = replay(history)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found for product: " + productNumber));

        StockEvent event = StockEvent.deleted(productNumber, nextVersion(history));
        stockEventRepository.save(event);

        stockRepository.deleteById(productNumber);
    }

    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    private List<StockEvent> loadHistory(String productNumber) {
        return stockEventRepository.findByProductNumberOrderByVersionAsc(productNumber);
    }

    private Optional<Stock> replay(List<StockEvent> events) {
        if (events.isEmpty()) {
            return Optional.empty();
        }

        Stock aggregate = null;
        for (StockEvent event : events) {
            aggregate = switch (event.getType()) {
                case CREATED -> new Stock(event.getProductNumber(), event.getQuantity());
                case UPDATED -> {
                    if (aggregate == null) {
                        throw new IllegalStateException("Received update event without an existing aggregate");
                    }
                    aggregate.setQuantity(event.getQuantity());
                    yield aggregate;
                }
                case DELETED -> null;
            };
        }

        return Optional.ofNullable(aggregate);
    }

    private long nextVersion(List<StockEvent> events) {
        return events.stream()
                .map(StockEvent::getVersion)
                .max(Comparator.naturalOrder())
                .orElse(0L) + 1;
    }
}

