package esb;

public class Order {
	private String orderNumber;
	private Integer orderId;
	private double amount;
	private String orderType; // "domestic" or "international"
	private String paymentType; // "mastercard", "visa", or "paypal"

	public Order() {
	}

	public Order(String orderNumber, double amount) {
		super();
		this.orderNumber = orderNumber;
		this.amount = amount;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	@Override
	public String toString() {
		return "Order{" +
				"orderNumber='" + orderNumber + '\'' +
				", orderId=" + orderId +
				", amount=" + amount +
				", orderType='" + orderType + '\'' +
				", paymentType='" + paymentType + '\'' +
				'}';
	}
}
