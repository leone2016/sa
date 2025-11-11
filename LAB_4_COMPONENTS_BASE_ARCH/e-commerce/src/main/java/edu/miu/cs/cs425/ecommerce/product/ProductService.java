package edu.miu.cs.cs425.ecommerce.product;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductService {
    private final ProductRepository repo;
    public ProductService(ProductRepository repo) { this.repo = repo; }

    public Product addProduct(Product p) {
        return repo.save(p);
    }

    public Product getProduct(String productNumber) {
        return repo.findByProductNumber(productNumber)
                   .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product updatePrice(String productNumber, BigDecimal price) {
        var p = getProduct(productNumber);
        p.setPrice(price);
        return repo.save(p);
    }
}
