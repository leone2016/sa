package shopping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import shopping.domain.Product;
import shopping.domain.ShoppingCart;
import shopping.repository.ShoppingCartRepository;
import shopping.service.dto.ProductDTO;
import shopping.service.dto.ShoppingCartAdapter;
import shopping.service.dto.ShoppingCartDTO;


import java.util.Optional;

@Service
public class ShoppingService {

	@Autowired
	ShoppingCartRepository shoppingCartRepository;
	@Autowired
	ProductsFeignClient productsFeignClient;



	public void addToCart(String cartId, String productnumber, int quantity)  {
		ResponseEntity<ProductDTO> responseEntity = productsFeignClient.getProduct(productnumber);
		ProductDTO productDto = responseEntity.getBody();

		//create a shopping product from a products product
		Product product = new Product(productDto.getProductnumber(),productDto.getDescription(),productDto.getPrice());
		Optional<ShoppingCart> cartOptional = shoppingCartRepository.findById(cartId);
		if (cartOptional.isPresent() && product != null) {
			ShoppingCart cart = cartOptional.get();
			cart.addToCart(product, quantity);
			shoppingCartRepository.save(cart);
		}
		else if (product != null) {
			ShoppingCart cart = new ShoppingCart();
			cart.setCartid(cartId);
			cart.addToCart(product, quantity);
			shoppingCartRepository.save(cart);
		}		
	}
	
	public ShoppingCartDTO getCart(String cartId) {
		Optional<ShoppingCart> cart = shoppingCartRepository.findById(cartId);
		if (cart.isPresent())
		  return ShoppingCartAdapter.getShoppingCartDTO(cart.get());
		else
			return null;
	}


	@FeignClient(name = "ProductService")
	interface ProductsFeignClient {
		@GetMapping("/products/{productnumber}")
		public ResponseEntity<ProductDTO> getProduct(@PathVariable String productnumber);
	}
}
