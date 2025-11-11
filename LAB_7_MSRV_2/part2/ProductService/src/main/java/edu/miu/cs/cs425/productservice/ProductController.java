package edu.miu.cs.cs425.productservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private StockServiceClient stockServiceClient;

    

    @GetMapping("{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
       Optional<Product> product = productRepository.getProducts()
                .stream()
                .filter(p->p.getId() == id)
                .findFirst();

         if(product.isPresent()){
                Product p = product.get();
                // Get stock from StockService
                int stock = stockServiceClient.getStockByProductNumber(p.getProductNumber());
                p.setNumberInStock(stock);
                return ResponseEntity.ok(p);
         }
         
        return ResponseEntity.notFound().build();
    }
}
