package esb;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @PostMapping("/pay")
    public ResponseEntity<?> processPayment(@RequestBody Order order) {
        System.out.println("💳 PayPal Payment Service processing payment: "+order);
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }
}

