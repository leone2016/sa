package edu.miu.cs.cs425.stockcommandservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.miu.cs.cs425.stockcommandservice.domain.StockEvent;

public interface StockEventRepository extends MongoRepository<StockEvent, String> {

    List<StockEvent> findByProductNumberOrderByVersionAsc(String productNumber);
}


