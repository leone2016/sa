package esb.payment;

import esb.MonitoringLogger;
import esb.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class MastercardPaymentActivator {
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
    MonitoringLogger monitoringLogger;

	public Order pay(Order order) {
		System.out.println("💳 Mastercard Payment: " + order.toString());
		restTemplate.postForLocation("http://localhost:8085/pay", order);
		monitoringLogger.logPaymentCompleted(order, "MastercardPaymentService");
		return order;
	}
}

