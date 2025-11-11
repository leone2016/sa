package edu.miu.cs.cs425.ecommerce.shopping;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Document("shoppingcarts")
public class ShoppingCart {
    @Id
    private String id;
    private List<CartItem> items = new ArrayList<>();

    public List<CartItem> getItems() { return items; }

    public void addProduct(CartItem item) {
        items.add(item);
    }

    public BigDecimal getTotal() {
        return items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
