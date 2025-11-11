package shop.products.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import shop.products.domain.Product;
import shop.products.service.dto.ProductChangeEventDTO;
import shop.products.repository.ProductRepository;
import shop.products.service.dto.ProductAdapter;
import shop.products.service.dto.ProductDTO;

@Service
public class ProductCatalogService implements IProductCatalogService {
	@Autowired
	ProductRepository productRepository;
	@Autowired
	private ApplicationEventPublisher publisher;


	@Override
	public void addProduct(ProductDTO productDto) {
		Product product = ProductAdapter.getProduct(productDto);
		//check if product exists
		Optional<Product> result = productRepository.findById(product.getProductnumber());
		if (result.isPresent())
			publisher.publishEvent(new ProductChangeEventDTO(productDto));
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
