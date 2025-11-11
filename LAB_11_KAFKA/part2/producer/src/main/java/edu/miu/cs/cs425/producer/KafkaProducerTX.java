package edu.miu.cs.cs425.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class KafkaProducerTX {
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Transactional
	public void sendOrderInTransaction() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		for(int i = 0; i < 10; i++){
			Order order = new Order("TX_ORDER_" + i, "Customer_" + i, "Country_" + i, i * 10.0, "NEW");
			String writeValueAsString = objectMapper.writeValueAsString(order);
			kafkaTemplate.send("orders_tx", "TX_ORDER_" + i, writeValueAsString);
			if(i > 5) {
				// throw new RuntimeException("Simulated error in transaction");
			}
			System.out.println("Sent order in transaction: " + writeValueAsString);
			Thread.sleep(1000);
		}
	}
}