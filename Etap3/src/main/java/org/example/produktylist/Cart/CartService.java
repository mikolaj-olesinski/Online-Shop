package org.example.produktylist.Cart;

import org.example.produktylist.Product.ProductService;
import org.example.produktylist.User.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;

    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    public Cart getCartByUser(User user) {
        return cartRepository.findByUserId((long) user.getId());
    }

    public void addToCart(Long productId, User user) {
        var product = productService.getProductById(productId);
        Cart cart = cartRepository.findByUserId((long) user.getId());

        if (cart == null) {
            cart = Cart.builder()
                    .user(user)
                    .build();
        }

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + 1);
        } else {
            cart.getItems().add(CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(1)
                    .build());
        }

        cartRepository.save(cart);
    }

    public void removeFromCart(Long productId, User user) {
        Cart cart = cartRepository.findByUserId((long) user.getId());
        if (cart != null) {
            cart.getItems().removeIf(item -> item.getProduct().getId() == productId);
            cartRepository.save(cart);
        }
    }

}
