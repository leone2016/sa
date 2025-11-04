package products;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import products.repository.ProductRepository;


@SpringBootApplication
@EnableDiscoveryClient
public class ProductApplication implements CommandLineRunner {
	@Autowired
	ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		productRepository.deleteAll();
	}
}
