package edu.miu.cs.cs425.productcommandservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.miu.cs.cs425.productcommandservice.domain.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

    boolean existsByName(String name);
}

