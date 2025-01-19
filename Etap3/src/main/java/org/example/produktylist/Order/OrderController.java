package org.example.produktylist.Order;

import org.example.produktylist.User.User;
import org.example.produktylist.User.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    // Widok wszystkich zamówień dla administratora
    @GetMapping("/orders")
    public String viewAllOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders/all";
    }

    // Widok zamówień dla konkretnego użytkownika
    @GetMapping("/orders/my-orders")
    public String viewUserOrders(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        List<Order> orders = orderService.getOrdersByUser(user);
        model.addAttribute("orders", orders);
        return "orders/my-orders";
    }

    // Możesz dodać więcej metod, takich jak tworzenie zamówienia lub zarządzanie statusem zamówienia
}
