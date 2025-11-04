package products.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import products.domain.Product;
import products.integration.MessageSender;
import products.repository.ProductRepository;
import products.service.dto.ProductAdapter;
import products.service.dto.ProductChangeEventDTO;
import products.service.dto.ProductDTO;
import java.util.Optional;

@Service
public class ProductCatalogService implements IProductCatalogService {
	@Autowired
	ProductRepository productRepository;

	@Autowired
	MessageSender jmsSender;


	@Override
	public void addProduct(ProductDTO productDto) {
		Product product = ProductAdapter.getProduct(productDto);
		//check if product exists
		Optional<Product> result = productRepository.findById(product.getProductnumber());
		if (result.isPresent())
			jmsSender.sendMessage(new ProductChangeEventDTO(productDto));
		productRepository.save(product);
		
	}
	@Override
	public ProductDTO getProduct(String productnumber) {
		Optional<Product> result = productRepository.findById(productnumber);
		if (result.isPresent())
		  return ProductAdapter.getProductDTO(result.get());
		else
			return null;
	}
}
