package edu.miu.cs.cs425.producer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProducerApplication implements CommandLineRunner {

    private final OrderProducer orderProducer;

    public ProducerApplication(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
         Order order1 = new Order("1001", "Alice", "USA", 250.75, "NEW");
         Order order2 = new Order("1002", "Bob", "UK", 150.50, "NEW");
         Order order3 = new Order("1003", "Charlie", "Canada", 300.00, "NEW");
         Order order4 = new Order("1004", "Diana", "Australia", 450.25, "NEW");
         Order order5 = new Order("1005", "Ethan", "Germany", 120.00, "NEW");
         orderProducer.sendOrder(order3);
         orderProducer.sendOrder(order1);
         orderProducer.sendOrder(order5);
         orderProducer.sendOrder(order2);
         orderProducer.sendOrder(order4);
    }
}
