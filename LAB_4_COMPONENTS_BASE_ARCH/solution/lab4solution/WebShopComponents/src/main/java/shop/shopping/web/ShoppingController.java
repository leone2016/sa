package shop.shopping.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.shopping.service.dto.AddToCartDTO;
import shop.shopping.service.dto.ShoppingCartDTO;
import shop.shopping.service.ShoppingService;

@RestController
public class ShoppingController {
	@Autowired
	ShoppingService shoppingService;
	
	@PostMapping(value = "/cart/{cartId}")
	public ResponseEntity<?> addToCart(@PathVariable String cartId,  @RequestBody AddToCartDTO addToCartDTO) {
		shoppingService.addToCart(cartId, addToCartDTO.getProductnumber(), addToCartDTO.getQuantity());
		return new ResponseEntity<ShoppingCartDTO>(HttpStatus.OK);		
	}
	
	@GetMapping("/cart/{cartId}")
	public ResponseEntity<?> getCart(@PathVariable String cartId) {
		ShoppingCartDTO cart = shoppingService.getCart(cartId);
		return new ResponseEntity<ShoppingCartDTO>(cart, HttpStatus.OK);
	}

	
}
