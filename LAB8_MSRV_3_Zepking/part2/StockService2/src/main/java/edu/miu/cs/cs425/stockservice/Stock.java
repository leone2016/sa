package edu.miu.cs.cs425.stockservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Stock {
    private String productNumber;
    private int quantity;
}
