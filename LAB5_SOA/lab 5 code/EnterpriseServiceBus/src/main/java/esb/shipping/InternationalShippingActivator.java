package esb.shipping;

import esb.MonitoringLogger;
import esb.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class InternationalShippingActivator {
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
    MonitoringLogger monitoringLogger;

	public Order ship(Order order) {
		System.out.println("🌍 International Shipping: " + order.toString());
		restTemplate.postForLocation("http://localhost:8084/orders", order);
		monitoringLogger.logShippingDispatched(order, "InternationalShippingService");
		return order;
	}
}

