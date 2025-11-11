package shopping.service.dto;

public class ProductChangeEventDTO {
    private ProductDTO product;

    public ProductChangeEventDTO() {
    }

    public ProductChangeEventDTO(ProductDTO product) {
        this.product = product;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }
}
