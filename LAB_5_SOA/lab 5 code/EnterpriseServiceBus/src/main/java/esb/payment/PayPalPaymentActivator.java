package esb.payment;

import esb.MonitoringLogger;
import esb.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class PayPalPaymentActivator {
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
    MonitoringLogger monitoringLogger;

	public void pay(Order order) {
		System.out.println("💳 PayPal Payment: " + order.toString());
		restTemplate.postForLocation("http://localhost:8087/pay", order);
		monitoringLogger.logPaymentCompleted(order, "PayPalPaymentService");
//		return order;
	}
}

