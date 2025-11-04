package products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ProductController {
	@Autowired
	ProductService productService;

	@RequestMapping("/product/{productNumber}")
	public ResponseEntity<?> getProduct(@PathVariable("productNumber") String productNumber) {
		Product product =productService.getProduct(productNumber);
		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}
}
