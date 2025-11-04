package products.service.dto;

public class ProductChangeEventDTO {
    private ProductDTO product;

    public ProductChangeEventDTO(ProductDTO product) {
        this.product = product;
    }

    public ProductDTO getProduct() {
        return product;
    }
}
