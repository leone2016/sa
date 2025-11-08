package edu.miu.cs.cs425.productqueryservice.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.miu.cs.cs425.productqueryservice.domain.ProductDocument;
import edu.miu.cs.cs425.productqueryservice.domain.StockDocument;
import edu.miu.cs.cs425.productqueryservice.repository.ProductReadRepository;
import edu.miu.cs.cs425.productqueryservice.repository.StockReadRepository;

@Service
@Transactional(readOnly = true)
public class ProductQueryService {

    private final ProductReadRepository productReadRepository;
    private final StockReadRepository stockReadRepository;

    public ProductQueryService(ProductReadRepository productReadRepository,
                               StockReadRepository stockReadRepository) {
        this.productReadRepository = productReadRepository;
        this.stockReadRepository = stockReadRepository;
    }

    public List<ProductView> getProducts() {
        List<ProductDocument> products = productReadRepository.findAll();
        Map<String, Integer> stockByProduct = stockReadRepository.findAll()
                .stream()
                .collect(Collectors.toMap(StockDocument::getProductNumber, StockDocument::getQuantity));

        return products.stream()
                .map(product -> new ProductView(
                        product.getProductNumber(),
                        product.getName(),
                        product.getPrice(),
                        stockByProduct.getOrDefault(product.getProductNumber(), 0)))
                .collect(Collectors.toList());
    }
}

