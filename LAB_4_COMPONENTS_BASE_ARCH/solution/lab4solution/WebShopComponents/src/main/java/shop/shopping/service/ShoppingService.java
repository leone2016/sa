package shop.shopping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.products.service.IProductCatalogService;
import shop.shopping.domain.Product;
import shop.shopping.domain.ShoppingCart;
import shop.shopping.repository.ShoppingCartRepository;
import shop.products.service.dto.ProductDTO;
import shop.shopping.service.dto.ShoppingCartAdapter;
import shop.shopping.service.dto.ShoppingCartDTO;

import java.util.Optional;

@Service
public class ShoppingService {

	@Autowired
	ShoppingCartRepository shoppingCartRepository;
	@Autowired
	IProductCatalogService productCatalogService;


	public void addToCart(String cartId, String productnumber, int quantity) {
		ProductDTO productDto = productCatalogService.getProduct(productnumber);
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


}
