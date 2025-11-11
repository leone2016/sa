package edu.miu.cs.cs425.ecommerce.shopping;

import edu.miu.cs.cs425.ecommerce.product.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingService {
    private final ShoppingCartRepository repo;
    private final ProductService productService;

    public ShoppingService(ShoppingCartRepository repo, ProductService productService) {
        this.repo = repo;
        this.productService = productService;
    }

    public ShoppingCart addToCart(String cartId, String productNumber, int qty) {
        var product = productService.getProduct(productNumber);
        var cart = repo.findById(cartId).orElse(new ShoppingCart());
        cart.addProduct(new CartItem(product, qty));
        return repo.save(cart);
    }

    public ShoppingCart getCart(String cartId) {
        return repo.findById(cartId).orElse(new ShoppingCart());
    }
}
