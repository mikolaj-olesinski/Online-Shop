package org.example.produktylist.Cart;

import org.example.produktylist.Product.ProductService;
import org.example.produktylist.User.User;
import org.example.produktylist.User.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    private final UserRepository userRepository; // Wstrzykujemy UserRepository

    public CartController(CartService cartService, ProductService productService, UserRepository userRepository) {
        this.cartService = cartService;
        this.productService = productService;
        this.userRepository = userRepository; // Inicjalizujemy UserRepository
    }

    @GetMapping("/")
    public String viewCart(Model model) {
        // Znajdź użytkownika o id 1 (dla testów)
        Long userId = 1L; // Id użytkownika, którego koszyk chcemy zobaczyć
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Pobierz koszyk dla użytkownika
        Cart cart = cartService.getCartByUser(user);

        // Oblicz całkowitą wartość koszyka
        double total = 0;
        if (cart != null) {
            total = cart.getItems().stream()
                    .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                    .sum();
        }

        // Dodaj dane do modelu
        model.addAttribute("cartItems", cart != null ? cart.getItems() : List.of());
        model.addAttribute("total", String.format("%.2f", total));
        return "cart/index";
    }

    @GetMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        cartService.addToCart(productId, user);
        return "redirect:/cart/";
    }

    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        cartService.removeFromCart(productId, user);
        return "redirect:/cart/";
    }
}
