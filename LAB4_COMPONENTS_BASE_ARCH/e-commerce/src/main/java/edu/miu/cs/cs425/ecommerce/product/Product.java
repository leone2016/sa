package edu.miu.cs.cs425.ecommerce.product;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

@Data
@Document("products")
public class Product {
    @Id
    private String id;
    private String productNumber;
    private String name;
    private String description;
    private BigDecimal price;
}
