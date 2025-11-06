package edu.miu.cs.cs425.stockservice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.ArrayList;
@SpringBootApplication
@EnableDiscoveryClient
public class StockRepository {

    public ArrayList<Stock> getStocks() {
        ArrayList<Stock> stock = new ArrayList<>();
        stock.add(new Stock("P1001",  10));
        stock.add(new Stock("P1002", 20));
        stock.add(new Stock("P1003", 15));
        return stock;
    }
}
