package edu.miu.cs.cs425.ecommerce.shopping;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/shopping")
public class ShoppingController {
    private final ShoppingService service;
    public ShoppingController(ShoppingService service) { this.service = service; }

    @PostMapping("/{cartId}/items")
    public ShoppingCart addItem(@PathVariable String cartId, @RequestBody Map<String, Object> body) {
        String productNumber = body.get("productNumber").toString();
        int qty = Integer.parseInt(body.get("quantity").toString());
        return service.addToCart(cartId, productNumber, qty);
    }

    @GetMapping("/{cartId}")
    public ShoppingCart getCart(@PathVariable String cartId) {
        return service.getCart(cartId);
    }
}
