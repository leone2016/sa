package edu.miu.cs.cs425.ecommerce;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
public class ECommerceApplication  implements CommandLineRunner {

    private final WebClient client = WebClient.create("http://localhost:8080/api");

    public static void main(String[] args) {
        SpringApplication.run(ECommerceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String productNumber = "P100-"+ new Random().nextInt(1000,9999);
        String cartId = "cart1";

        System.out.println("1️⃣ Adding product...");
        var product = client.post().uri("/products")
                .bodyValue(Map.of(
                        "productNumber", productNumber,
                        "name", "Laptop",
                        "description", "Gaming laptop",
                        "price", new BigDecimal("1200")
                ))
                .retrieve().bodyToMono(Map.class).block();
        System.out.println(product);

        System.out.println("2️⃣ Getting product...");
        var fetched = client.get().uri("/products/{id}", productNumber)
                .retrieve().bodyToMono(Map.class).block();
        System.out.println(fetched);

        System.out.println("3️⃣ Adding to shopping cart...");
        client.post().uri("/shopping/{cartId}/items", cartId)
                .bodyValue(Map.of("productNumber", productNumber, "quantity", 2))
                .retrieve().bodyToMono(Map.class).block();

        System.out.println("4️⃣ Getting shopping cart...");
        var cart = client.get().uri("/shopping/{cartId}", cartId)
                .retrieve().bodyToMono(Map.class).block();
        System.out.println(cart);

        System.out.println("5️⃣ Changing product price...");
        client.patch().uri("/products/{id}/price", productNumber)
                .bodyValue(Map.of("price", new BigDecimal("1400")))
                .retrieve().bodyToMono(Map.class).block();

        System.out.println("6️⃣ Getting shopping cart (price updated)...");
        var updatedCart = client.get().uri("/shopping/{cartId}", cartId)
                .retrieve().bodyToMono(Map.class).block();
        System.out.println(updatedCart);
    }
}
