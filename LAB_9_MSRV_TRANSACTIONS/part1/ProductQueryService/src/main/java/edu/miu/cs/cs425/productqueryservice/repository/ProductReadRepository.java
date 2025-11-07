package edu.miu.cs.cs425.productqueryservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.miu.cs.cs425.productqueryservice.domain.ProductDocument;

public interface ProductReadRepository extends MongoRepository<ProductDocument, String> {
}

