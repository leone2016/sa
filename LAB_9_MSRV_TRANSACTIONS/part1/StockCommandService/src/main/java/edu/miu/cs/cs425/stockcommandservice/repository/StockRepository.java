package edu.miu.cs.cs425.stockcommandservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.miu.cs.cs425.stockcommandservice.domain.Stock;

public interface StockRepository extends MongoRepository<Stock, String> {
}

