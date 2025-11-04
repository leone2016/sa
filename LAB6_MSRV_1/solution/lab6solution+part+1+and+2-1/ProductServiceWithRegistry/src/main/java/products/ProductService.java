package products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
public class ProductService {
	@Autowired
	StockFeignClient stockClient;

	public Product getProduct(String productNumber) {
		int stock = stockClient.getStock(productNumber);
		Product product = new Product("IPhone 11", productNumber);
		product.setNumberOnStock(stock);
		return product;
	}

	@FeignClient("StockService")
	interface StockFeignClient {
		@RequestMapping("/stock/{productNumber}")
		public Integer getStock(@PathVariable("productNumber") String productNumber);
	}

}
