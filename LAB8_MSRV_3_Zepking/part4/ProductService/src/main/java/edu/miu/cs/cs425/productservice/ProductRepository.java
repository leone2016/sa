package edu.miu.cs.cs425.productservice;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class ProductRepository {

    public ArrayList<Product> getProducts() {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product(1, "P1001", "Laptop", 0));
        products.add(new Product(2, "P1002", "Smartphone", 0));
        products.add(new Product(3, "P1003", "Tablet", 0));
        return products;
    }
}
