package shop.products.service.dto;

import shop.products.service.dto.ProductDTO;

public class ProductChangeEventDTO {
    private ProductDTO product;
    // No-arg constructor required for Jackson deserialization
    public ProductChangeEventDTO() {
    }

    public ProductChangeEventDTO(ProductDTO product) {
        this.product = product;
    }

    public ProductDTO getProduct() {
        return product;
    }

    // Setter required for Jackson to set the value when deserializing
    public void setProduct(ProductDTO product) {
        this.product = product;
    }
}
