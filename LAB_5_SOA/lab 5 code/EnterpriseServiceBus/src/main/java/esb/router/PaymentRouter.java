package esb.router;

import esb.Order;
import org.springframework.integration.annotation.Router;
import org.springframework.messaging.Message;

import java.util.Map;
import java.util.Optional;

public class PaymentRouter {

    @Router
    public String route(Message<Order> message) {
        Order order = message.getPayload();

        // Define the map of payment types to channels
        Map<String, String> paymentRoutes = Map.of(
                "mastercard", "mastercardPaymentChannel",
                "visa", "visaPaymentChannel",
                "paypal", "paypalPaymentChannel"
        );

        // Normalizing the payment type (avoids NPE and uppercase)
        String paymentType = Optional.ofNullable(order.getPaymentType())
                .map(String::toLowerCase)
                .orElse("visa");

        // Return the corresponding channel or a default one
        return paymentRoutes.getOrDefault(paymentType, "visaPaymentChannel");
    }
}

