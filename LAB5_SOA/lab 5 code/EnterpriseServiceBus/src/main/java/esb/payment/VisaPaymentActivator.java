package esb.payment;

import esb.MonitoringLogger;
import esb.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class VisaPaymentActivator {
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
    MonitoringLogger monitoringLogger;

	public Order pay(Order order) {
		System.out.println("💳 Visa Payment: " + order.toString());
		restTemplate.postForLocation("http://localhost:8086/pay", order);
		monitoringLogger.logPaymentCompleted(order, "VisaPaymentService");
		return order;
	}
}

