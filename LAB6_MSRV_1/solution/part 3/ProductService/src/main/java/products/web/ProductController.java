package products.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import products.domain.Product;
import products.service.ProductCatalogService;
import products.service.dto.ProductDTO;


@RestController
public class ProductController {
	@Autowired
	ProductCatalogService productCatalogService;

	@GetMapping("/products/{productnumber}")
	public ResponseEntity<ProductDTO> getProduct(@PathVariable String productnumber) {
		ProductDTO productDTO = productCatalogService.getProduct(productnumber);
		return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.OK);
	}

	@PostMapping(value = "/products")
	public ResponseEntity<?> addProduct(@RequestBody ProductDTO productDto) {
		productCatalogService.addProduct(productDto);
		return new ResponseEntity<ProductDTO>(productDto, HttpStatus.OK);
	}

}
