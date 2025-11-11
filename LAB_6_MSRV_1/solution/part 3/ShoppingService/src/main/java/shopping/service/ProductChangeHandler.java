package shopping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shopping.domain.Product;
import shopping.domain.ShoppingCart;
import shopping.repository.ShoppingCartRepository;

import java.util.Optional;

@Component
public class ProductChangeHandler {
    @Autowired
    ShoppingService shoppingService;
    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    public void handleProductChange(Product product){
        Optional<ShoppingCart> cartOptional = shoppingCartRepository.findById("1");
          if (cartOptional.isPresent()){
              ShoppingCart cart = cartOptional.get();
              cart.updateProduct(product);
              shoppingCartRepository.save(cart);
          }
    }
}
