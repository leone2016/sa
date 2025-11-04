package shop.products.service.dto;

import shop.products.service.dto.ProductDTO;

public class ProductChangeEventDTO {
    private ProductDTO product;

    public ProductChangeEventDTO(ProductDTO product) {
        this.product = product;
    }

    public ProductDTO getProduct() {
        return product;
    }
}
