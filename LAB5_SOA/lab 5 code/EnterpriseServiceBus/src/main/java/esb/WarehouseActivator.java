
package esb;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class WarehouseActivator {

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	MonitoringLogger monitoringLogger;

	public Order checkStock(Order order) {
		System.out.println(" 🟢 WarehouseService: checking order "+order.toString());
		restTemplate.postForLocation("http://localhost:8081/orders", order);
		monitoringLogger.logWarehouseProcessed(order);
		return order;
	}
}
