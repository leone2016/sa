package edu.miu.cs.cs425.stockservice;

import java.util.ArrayList;

public class StockRepository {

    public ArrayList<Stock> getStocks() {
        ArrayList<Stock> stock = new ArrayList<>();
        stock.add(new Stock("P1001",  10));
        stock.add(new Stock("P1002", 20));
        stock.add(new Stock("P1003", 15));
        return stock;
    }
}
