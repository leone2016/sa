package products.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import products.domain.Product;


public interface ProductRepository extends MongoRepository<Product, String> {

}
