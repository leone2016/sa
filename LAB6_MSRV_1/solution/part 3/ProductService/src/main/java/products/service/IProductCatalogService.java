package products.service;


import products.service.dto.ProductDTO;

public interface IProductCatalogService {
    void addProduct(ProductDTO productDto);

    ProductDTO getProduct(String productnumber);
}
