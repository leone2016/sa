package shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import static java.lang.Thread.sleep;


@SpringBootApplication
public class WebShopApplication implements CommandLineRunner {

	private RestTemplate  restTemplate = new RestTemplate();

	public static void main(String[] args) {
		SpringApplication.run(WebShopApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		//create products
		restTemplate.postForLocation("http://localhost:8090/products", new ProductDTO("A33","TV",450.0));
		restTemplate.postForLocation("http://localhost:8090/products", new ProductDTO("A34","MP3 Player",75.0));

		//add product to the shoppingcart
		restTemplate.postForLocation("http://localhost:8091/cart/1/A33/3",null);
		//add product to the shoppingcart
		restTemplate.postForLocation("http://localhost:8091/cart/1/A34/2", null);

		//get the shoppingcart
		ShoppingCartDTO cart = restTemplate.getForObject("http://localhost:8091/cart/1", ShoppingCartDTO.class);
		System.out.println("\n-----Shoppingcart-------");
		if (cart != null) cart.print();

		//change product price
		restTemplate.postForLocation("http://localhost:8090/products", new ProductDTO("A33","TV",550.0));

		//wait till event has been processed
		sleep(1000);

		//get the shoppingcart
		cart = restTemplate.getForObject("http://localhost:8091/cart/1", ShoppingCartDTO.class);
		System.out.println("\n-----Shoppingcart after price change-------");
		if (cart != null) cart.print();
	}


}
