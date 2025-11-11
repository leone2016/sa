package shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;



@SpringBootApplication
public class WebShopApplication implements CommandLineRunner {

	private RestTemplate  restTemplate = new RestTemplate();

	public static void main(String[] args) {
		SpringApplication.run(WebShopApplication.class, args);
	}



	@Override
	public void run(String... args) throws Exception {

		//create products
		restTemplate.postForLocation("http://localhost:8080/products", new ProductDTO("A33","TV",450.0));
		restTemplate.postForLocation("http://localhost:8080/products", new ProductDTO("A34","MP3 Player",75.0));

		//add product to the shoppingcart
		restTemplate.postForLocation("http://localhost:8080/cart/1",new AddToCartDTO("A33",3));
		//add product to the shoppingcart
		restTemplate.postForLocation("http://localhost:8080/cart/1", new AddToCartDTO("A34",2));

		//get the shoppingcart
		ShoppingCartDTO cart = restTemplate.getForObject("http://localhost:8080/cart/1", ShoppingCartDTO.class);
		System.out.println("\n-----Shoppingcart-------");
		if (cart != null) cart.print();

		//change product price
		restTemplate.postForLocation("http://localhost:8080/products", new ProductDTO("A33","TV",550.0));

		//get the shoppingcart
		cart = restTemplate.getForObject("http://localhost:8080/cart/1", ShoppingCartDTO.class);
		System.out.println("\n-----Shoppingcart after price change-------");
		if (cart != null) cart.print();
	}


}
