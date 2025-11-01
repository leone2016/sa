package esb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class MonitoringLogger {
	@Autowired
	RestTemplate restTemplate;

	public void log(String message) {
		System.out.println("[MONITOR] " + message);
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.TEXT_PLAIN);
			HttpEntity<String> entity = new HttpEntity<>(message, headers);
			restTemplate.postForEntity("http://localhost:8088/log", entity, String.class);
		} catch (Exception e) {
			// If monitoring service is not available, just log locally
			System.err.println("Monitoring service unavailable: " + e.getMessage());
		}
	}

	public void logOrderReceived(Order order) {
		String logMsg = "Order " + (order.getOrderId() != null ? order.getOrderId() : order.getOrderNumber()) + " received by OrderController";
		log(logMsg);
	}

	public void logWarehouseProcessed(Order order) {
		String logMsg = "Processed by WarehouseService";
		log(logMsg);
	}

	public void logShippingDispatched(Order order, String shippingType) {
		String logMsg = "Routed to " + shippingType;
		log(logMsg);
	}

	public void logPaymentCompleted(Order order, String paymentType) {
		String logMsg = "Paid through " + paymentType;
		log(logMsg);
	}
}

