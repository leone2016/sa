package edu.miu.cs.cs425.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class Order {
	private String ordernumber;
	private String customername;
	private String customercountry;
	private double amount;
	private String status;
}

