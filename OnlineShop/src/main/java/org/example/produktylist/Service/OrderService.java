package org.example.produktylist.Service;

import org.example.produktylist.Entity.Order;
import org.example.produktylist.Repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Serwis obsługujący zamówienia.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zamówienia o ID: " + id));
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

}