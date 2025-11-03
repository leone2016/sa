package edu.miu.cs.cs425.productservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    private int id;
    private String productNumber;
    private String name;
    private int numberInStock;
}
