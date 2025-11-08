package edu.miu.cs.cs425.productqueryservice.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.miu.cs.cs425.productqueryservice.service.ProductQueryService;
import edu.miu.cs.cs425.productqueryservice.service.ProductView;

@RestController
@RequestMapping("/products")
public class ProductQueryController {

    private final ProductQueryService productQueryService;

    public ProductQueryController(ProductQueryService productQueryService) {
        this.productQueryService = productQueryService;
    }

    @GetMapping
    public List<ProductView> getProducts() {
        return productQueryService.getProducts();
    }
}

