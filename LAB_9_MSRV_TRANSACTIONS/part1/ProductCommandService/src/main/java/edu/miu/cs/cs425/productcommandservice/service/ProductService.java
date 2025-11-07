package edu.miu.cs.cs425.productcommandservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.miu.cs.cs425.productcommandservice.domain.Product;
import edu.miu.cs.cs425.productcommandservice.repository.ProductRepository;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(Product product) {
        if (productRepository.existsById(product.getProductNumber())) {
            throw new IllegalArgumentException("Product with number %s already exists".formatted(product.getProductNumber()));
        }
        return productRepository.save(product);
    }

    public Product update(String productNumber, Product product) {
        Product existing = productRepository.findById(productNumber)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productNumber));
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        return productRepository.save(existing);
    }

    public void delete(String productNumber) {
        if (!productRepository.existsById(productNumber)) {
            throw new IllegalArgumentException("Product not found: " + productNumber);
        }
        productRepository.deleteById(productNumber);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
}

