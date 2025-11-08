package edu.miu.cs.cs425.productcommandservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.miu.cs.cs425.productcommandservice.domain.ProductEvent;

public interface ProductEventRepository extends MongoRepository<ProductEvent, String> {

    List<ProductEvent> findByProductNumberOrderByVersionAsc(String productNumber);
}


