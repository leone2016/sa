package shopping;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import shopping.repository.ShoppingCartRepository;


@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class ShoppingApplication implements CommandLineRunner {
	@Autowired
	ShoppingCartRepository shoppingCartRepository;

	public static void main(String[] args) {
		SpringApplication.run(ShoppingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		shoppingCartRepository.deleteAll();
	}
}
