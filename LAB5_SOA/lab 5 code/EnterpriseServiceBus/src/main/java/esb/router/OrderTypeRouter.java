package esb.router;

import esb.Order;
import org.springframework.integration.annotation.Router;
import org.springframework.messaging.Message;

import java.util.Map;
import java.util.Optional;

public class OrderTypeRouter {

    @Router
    public String route(Message<Order> message) {
        Order order = message.getPayload();

        // Using Map.of for simplicity and immutability
        Map<String, String> routeMap = Map.of(
                "international", "internationalShippingChannel",
                "domestic", "domesticRouterChannel"
        );

        // normalize type to lowercase and handle nulls
        String orderType = Optional.ofNullable(order.getOrderType())
                .map(String::toLowerCase)
                .orElse("domestic");

        // Search for the route, using 'domesticRouterChannel' as fallback
        return routeMap.getOrDefault(orderType, "domesticRouterChannel");
    }
}

