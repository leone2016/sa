package esb.shipping;

import esb.MonitoringLogger;
import esb.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class NormalShippingActivator {
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
    MonitoringLogger monitoringLogger;

	public Order ship(Order order) {
		System.out.println("🚚 Normal Shipping: "+ order.toString());
		restTemplate.postForLocation("http://localhost:8082/orders", order);
		monitoringLogger.logShippingDispatched(order, "NormalShippingService");
		return order;
	}
}

