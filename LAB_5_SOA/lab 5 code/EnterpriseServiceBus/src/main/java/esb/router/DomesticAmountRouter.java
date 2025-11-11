package esb.router;

import esb.Order;
import org.springframework.integration.annotation.Router;
import org.springframework.messaging.Message;

public class DomesticAmountRouter {
    
    @Router
    public String route(Message<Order> message) {
        Order order = message.getPayload();
        double amount = order.getAmount();
        
        if (amount <= 175.0) {
            return "normalShippingChannel";
        } else {
            return "nextDayShippingChannel";
        }
    }
}

