package shop.products.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
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
	// @Autowired
	// private ApplicationEventPublisher publisher;
	@Autowired
	private KafkaTemplate<String, ProductChangeEventDTO> kafkaTemplate;

	@Override
	public void addProduct(ProductDTO productDto) {
		System.out.println("Inside addProduct of ProductCatalogService for productnumber: " + productDto.getProductnumber());
		Product product = ProductAdapter.getProduct(productDto);
		//check if product exists
		Optional<Product> result = productRepository.findById(product.getProductnumber());
		productRepository.findAll().forEach(p -> System.out.println("Existing product: " + p.getProductnumber()));
		System.out.println("Product exists check for productnumber " + productDto.getProductnumber() + ": " + result.isPresent());
		if (result.isPresent()){
			System.out.println("sending stuff to kafka " + productDto.getProductnumber());
			kafkaTemplate.send("product", new ProductChangeEventDTO(productDto));
			// publisher.publishEvent(new ProductChangeEventDTO(productDto));
		}
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
