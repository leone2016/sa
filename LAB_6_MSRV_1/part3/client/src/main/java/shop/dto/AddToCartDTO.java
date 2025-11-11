package shop;

public class AddToCartDTO {
    private String productnumber;
    private int quantity;

    public AddToCartDTO() {
    }

    public AddToCartDTO(String productnumber, int quantity) {
        this.productnumber = productnumber;
        this.quantity = quantity;
    }

    public String getProductnumber() {
        return productnumber;
    }

    public void setProductnumber(String productnumber) {
        this.productnumber = productnumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
