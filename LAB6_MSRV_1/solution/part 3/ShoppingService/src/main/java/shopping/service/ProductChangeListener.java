package shopping.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import shopping.domain.Product;
import shopping.service.dto.ProductChangeEventDTO;
import shopping.service.dto.ProductDTO;


@Component
public class ProductChangeListener {
    @Autowired
    ProductChangeHandler productChangeHandler;

    @KafkaListener(topics = "testQueue")
    public void receiveMessage(final String productAsString) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductChangeEventDTO event = objectMapper.readValue(productAsString, ProductChangeEventDTO.class);
            ProductDTO productDto = event.getProduct();
            //create a shopping product from a products product
            Product product = new Product(productDto.getProductnumber(),productDto.getDescription(),productDto.getPrice());
            System.out.println("Receiver received message with product :" + product);
            productChangeHandler.handleProductChange(product);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
