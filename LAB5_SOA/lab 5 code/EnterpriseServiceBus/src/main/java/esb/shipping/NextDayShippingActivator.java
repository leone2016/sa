package esb.shipping;

import esb.MonitoringLogger;
import esb.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class NextDayShippingActivator {
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	MonitoringLogger monitoringLogger;

	public Order ship(Order order) {
		System.out.println("🚀 Next Day Shipping: "+ order.toString());
		restTemplate.postForLocation("http://localhost:8083/orders", order);
		monitoringLogger.logShippingDispatched(order, "NextDayShippingService");
		return order;
	}
}

