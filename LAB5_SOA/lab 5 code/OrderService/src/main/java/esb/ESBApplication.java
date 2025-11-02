package esb;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class ESBApplication implements CommandLineRunner {
	private final String ESB_ORDER_API_URL = "http://localhost:8080/api/orders";
	RestTemplate restTemplate = new RestTemplate();

	public static void main(String[] args) {
		SpringApplication.run(ESBApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		// Test order 1: Domestic order with amount <= 175, Visa payment
		Order order1 = new Order();
		order1.setOrderId(101);
		order1.setAmount(120.0);
		order1.setOrderType("domestic");
		order1.setPaymentType("visa");
		restTemplate.postForLocation(ESB_ORDER_API_URL, order1);
		
		// Test order 2: Domestic order with amount > 175, Mastercard payment
		Order order2 = new Order();
		order2.setOrderId(102);
		order2.setAmount(220.50);
		order2.setOrderType("domestic");
		order2.setPaymentType("mastercard");
		restTemplate.postForLocation(ESB_ORDER_API_URL, order2);

		// Test order 3: International order, PayPal payment
		Order order3 = new Order();
		order3.setOrderId(103);
		order3.setAmount(350.0);
		order3.setOrderType("international");
		order3.setPaymentType("paypal");
		restTemplate.postForLocation(ESB_ORDER_API_URL, order3);
	}
}
