package shopping.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import shopping.domain.ShoppingCart;

public interface ShoppingCartRepository extends MongoRepository<ShoppingCart, String> {

}
