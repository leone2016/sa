package edu.miu.cs.cs425.productcommandservice.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.miu.cs.cs425.productcommandservice.domain.Product;
import edu.miu.cs.cs425.productcommandservice.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> create(@Validated @RequestBody ProductRequest request) {
        Product created = productService.create(new Product(request.getProductNumber(), request.getName(), request.getPrice()));
        return ResponseEntity.created(URI.create("/products/" + created.getProductNumber())).body(created);
    }

    @PutMapping("/{productNumber}")
    public ResponseEntity<Product> update(@PathVariable String productNumber,
                                          @Validated @RequestBody ProductRequest request) {
        Product updated = productService.update(productNumber,
                new Product(productNumber, request.getName(), request.getPrice()));
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{productNumber}")
    public ResponseEntity<Void> delete(@PathVariable String productNumber) {
        productService.delete(productNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Product> findAll() {
        return productService.findAll();
    }
}

