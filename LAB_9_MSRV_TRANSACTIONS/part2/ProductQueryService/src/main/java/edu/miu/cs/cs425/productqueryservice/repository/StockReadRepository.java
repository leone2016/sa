package edu.miu.cs.cs425.productqueryservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.miu.cs.cs425.productqueryservice.domain.StockDocument;

public interface StockReadRepository extends MongoRepository<StockDocument, String> {
}

