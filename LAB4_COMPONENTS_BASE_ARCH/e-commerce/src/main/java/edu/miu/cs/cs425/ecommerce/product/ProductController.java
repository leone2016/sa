package edu.miu.cs.cs425.ecommerce.product;

import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service) { this.service = service; }

    @PostMapping
    public Product add(@RequestBody Product p) {
        return service.addProduct(p);
    }

    @GetMapping("/{productNumber}")
    public Product get(@PathVariable String productNumber) {
        return service.getProduct(productNumber);
    }

    @PatchMapping("/{productNumber}/price")
    public Product updatePrice(@PathVariable String productNumber, @RequestBody Map<String, Object> body) {
        BigDecimal newPrice = new BigDecimal(body.get("price").toString());
        return service.updatePrice(productNumber, newPrice);
    }
}
