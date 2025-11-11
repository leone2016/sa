package edu.miu.cs.cs425.ecommerce.shopping;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShoppingCartRepository extends MongoRepository<ShoppingCart, String> {}
