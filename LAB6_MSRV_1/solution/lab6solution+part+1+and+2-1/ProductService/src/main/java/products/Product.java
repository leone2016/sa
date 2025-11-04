package products;

public class Product {
	private String name;
	private String productNumber;
	private int numberOnStock;
	
	public Product(String name, String productNumber) {
		super();
		this.name = name;
		this.productNumber = productNumber;
	}
	public Product() {

	}
	public int getNumberOnStock() {
		return numberOnStock;
	}
	public void setNumberOnStock(int numberOnStock) {
		this.numberOnStock = numberOnStock;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
