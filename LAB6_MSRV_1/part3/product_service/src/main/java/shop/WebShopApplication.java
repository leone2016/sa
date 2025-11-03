package shop;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
// import org.springframework.jms.annotation.EnableJms;
// import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
// import org.springframework.jms.support.converter.MessageConverter;
// import org.springframework.jms.support.converter.MessageType;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.web.client.RestTemplate;
import shop.products.repository.ProductRepository;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.apache.kafka.clients.admin.NewTopic;

@SpringBootApplication
@EnableFeignClients
public class WebShopApplication implements CommandLineRunner {
	@Autowired
	ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(WebShopApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		productRepository.deleteAll();
		System.out.println("deleted all products");
	}

	@Bean
	public NewTopic productTopic() {
		return TopicBuilder.name("product").build();
	}
}
