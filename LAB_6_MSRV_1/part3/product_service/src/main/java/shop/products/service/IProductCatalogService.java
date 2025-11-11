package shop.products.service;

import shop.products.service.dto.ProductDTO;

public interface IProductCatalogService {
    void addProduct(ProductDTO productDto);

    ProductDTO getProduct(String productnumber);
}
