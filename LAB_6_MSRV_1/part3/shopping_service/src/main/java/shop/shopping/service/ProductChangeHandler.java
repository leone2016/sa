package shop.shopping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shop.shopping.domain.Product;
import shop.shopping.domain.ShoppingCart;
import shop.shopping.repository.ShoppingCartRepository;

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
              shoppingCartRepository.findById("1").ifPresent(updatedCart -> {
                  System.out.println("Updated cart after product change:");
                  updatedCart.print();
              });
          }
    }
}
