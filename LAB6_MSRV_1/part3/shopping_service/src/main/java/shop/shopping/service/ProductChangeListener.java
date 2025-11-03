package shop.shopping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import shop.products.service.dto.ProductChangeEventDTO;
import shop.products.service.dto.ProductDTO;
import shop.shopping.domain.Product;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.messaging.handler.annotation.Payload;
@Component
@KafkaListener(topics = "product")
public class ProductChangeListener {
    @Autowired
    ProductChangeHandler productChangeHandler;
    @KafkaHandler
    public void onEvent(@Payload ProductChangeEventDTO event) {
        System.out.println("Received ProductChangeEventDTO for productnumber: " + event.getProduct().getProductnumber());
        ProductDTO productDto = event.getProduct();
        //create a shopping product from a products product
        Product product = new Product(productDto.getProductnumber(),productDto.getDescription(),productDto.getPrice());
        productChangeHandler.handleProductChange(product);
    }
}
