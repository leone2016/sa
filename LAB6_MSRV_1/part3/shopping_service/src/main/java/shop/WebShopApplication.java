package shop;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
// import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
// import org.springframework.jms.annotation.EnableJms;
// import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
// import org.springframework.jms.support.converter.MessageConverter;
// import org.springframework.jms.support.converter.MessageType;
import org.springframework.web.client.RestTemplate;
import shop.shopping.repository.ShoppingCartRepository;


@SpringBootApplication
@EnableFeignClients
public class WebShopApplication implements CommandLineRunner {
	@Autowired
	ShoppingCartRepository shoppingCartRepository;

	public static void main(String[] args) {
		SpringApplication.run(WebShopApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		shoppingCartRepository.deleteAll();
		System.out.println("deleted all cart");
	}
}
