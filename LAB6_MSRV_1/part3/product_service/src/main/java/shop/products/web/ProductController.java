package shop.products.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import shop.products.domain.Product;
import shop.products.service.ProductCatalogService;
import shop.products.service.dto.ProductDTO;

@RestController
public class ProductController {
	@Autowired
	ProductCatalogService productCatalogService;

	@GetMapping("/products/{productnumber}")
	public ResponseEntity<?> getProduct(@PathVariable String productnumber) {
		System.out.println("Getting product with productnumber: " + productnumber);
		ProductDTO productDTO = productCatalogService.getProduct(productnumber);
		return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.OK);
	}

	@PostMapping(value = "/products")
	public ResponseEntity<?> addProduct(@RequestBody ProductDTO productDto) {
		System.out.println("Adding product with productnumber: " + productDto.getProductnumber());
		productCatalogService.addProduct(productDto);
		return new ResponseEntity<Product>(HttpStatus.OK);
	}

}
