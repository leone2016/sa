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
import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
public class ShoppingService {

	@Autowired
	ShoppingCartRepository shoppingCartRepository;
	// @Autowired
	// IProductCatalogService productCatalogService;
	private final ProductClient productClient;

	public ShoppingService(ProductClient productClient) {
		this.productClient = productClient;
	}

	public void addToCart(String cartId, String productnumber, int quantity) {
		// ProductDTO productDto = productClient.getProduct(productnumber);
		System.out.println("Fetching product with productnumber: " + productnumber);
		ProductDTO productDto = productClient.getProduct(productnumber);
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
		if (cart.isPresent()) {
		  return ShoppingCartAdapter.getShoppingCartDTO(cart.get());
		} else {
			return null;
		}
	}
}

@FeignClient(name = "product-service", url = "${product.service.url}")
interface ProductClient {
	@RequestMapping(method = RequestMethod.GET, value = "/products/{productNumber}")
	ProductDTO getProduct(@PathVariable("productNumber") String productNumber);
}