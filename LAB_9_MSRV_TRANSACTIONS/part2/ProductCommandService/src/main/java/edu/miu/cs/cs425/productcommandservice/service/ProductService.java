package edu.miu.cs.cs425.productcommandservice.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.miu.cs.cs425.productcommandservice.domain.Product;
import edu.miu.cs.cs425.productcommandservice.domain.ProductEvent;
import edu.miu.cs.cs425.productcommandservice.domain.ProductEventType;
import edu.miu.cs.cs425.productcommandservice.repository.ProductEventRepository;
import edu.miu.cs.cs425.productcommandservice.repository.ProductRepository;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductEventRepository productEventRepository;

    public ProductService(ProductRepository productRepository,
                          ProductEventRepository productEventRepository) {
        this.productRepository = productRepository;
        this.productEventRepository = productEventRepository;
    }

    public Product create(Product product) {
        List<ProductEvent> history = loadHistory(product.getProductNumber());
        if (replay(history).isPresent()) {
            throw new IllegalArgumentException("Product with number %s already exists".formatted(product.getProductNumber()));
        }

        ProductEvent event = ProductEvent.created(
                product.getProductNumber(),
                product.getName(),
                product.getPrice(),
                nextVersion(history));

        productEventRepository.save(event);

        return productRepository.save(product);
    }

    public Product update(String productNumber, Product product) {
        List<ProductEvent> history = loadHistory(productNumber);
        Product current = replay(history)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productNumber));

        current.setName(product.getName());
        current.setPrice(product.getPrice());

        ProductEvent event = ProductEvent.updated(
                productNumber,
                product.getName(),
                product.getPrice(),
                nextVersion(history));

        productEventRepository.save(event);

        return productRepository.save(current);
    }

    public void delete(String productNumber) {
        List<ProductEvent> history = loadHistory(productNumber);
        Product current = replay(history)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productNumber));

        ProductEvent event = ProductEvent.deleted(productNumber, nextVersion(history));
        productEventRepository.save(event);

        productRepository.deleteById(productNumber);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    private List<ProductEvent> loadHistory(String productNumber) {
        return productEventRepository.findByProductNumberOrderByVersionAsc(productNumber);
    }

    private Optional<Product> replay(List<ProductEvent> events) {
        if (events.isEmpty()) {
            return Optional.empty();
        }

        Product aggregate = null;
        for (ProductEvent event : events) {
            aggregate = switch (event.getType()) {
                case CREATED -> new Product(event.getProductNumber(), event.getName(), event.getPrice());
                case UPDATED -> {
                    if (aggregate == null) {
                        throw new IllegalStateException("Received update event without an existing aggregate");
                    }
                    aggregate.setName(event.getName());
                    aggregate.setPrice(event.getPrice());
                    yield aggregate;
                }
                case DELETED -> null;
            };
        }

        return Optional.ofNullable(aggregate);
    }

    private long nextVersion(List<ProductEvent> events) {
        return events.stream()
                .map(ProductEvent::getVersion)
                .max(Comparator.naturalOrder())
                .orElse(0L) + 1;
    }
}

