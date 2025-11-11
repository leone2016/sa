package edu.miu.cs.cs425.producer;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProducerApplication implements CommandLineRunner {

    private final OrderProducer orderProducer;
    private final String partitioningMode;

    public ProducerApplication(OrderProducer orderProducer,
            @Value("${orders.partitioning.mode:single-key}") String partitioningMode) {
        this.orderProducer = orderProducer;
        this.partitioningMode = partitioningMode;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<Order> sampleOrders = List.of(
                new Order("1001", "Alice", "USA", 250.75, "NEW"),
                new Order("1002", "Bob", "UK", 150.50, "NEW"),
                new Order("1003", "Charlie", "Canada", 300.00, "PROCESSING"),
                new Order("1004", "Diana", "Australia", 450.25, "SHIPPED"),
                new Order("1005", "Ethan", "Germany", 120.00, "DELIVERED")
        );

        switch (partitioningMode) {
            case "unique-key":
                for (Order order : sampleOrders) {
                    orderProducer.sendOrder(order, order.getOrdernumber());
                }
                break;
            case "single-key":
            default:
                for (Order order : sampleOrders) {
                    orderProducer.sendOrder(order, "ALL_ORDERS");
                }
                break;
        }
    }
}
