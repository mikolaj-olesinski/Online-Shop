package org.example.produktylist.Order;

import org.example.produktylist.Cart.Cart;
import org.example.produktylist.User.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Metoda do utworzenia nowego zamówienia
    public Order createOrder(User user, Cart cart) {
        Order order = Order.builder()
                .user(user)
                .cart(cart)
                .build();
        return orderRepository.save(order);
    }

    // Metoda do pobierania zamówień użytkownika
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserId(user.getId());
    }

    // Metoda do pobrania wszystkich zamówień
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
