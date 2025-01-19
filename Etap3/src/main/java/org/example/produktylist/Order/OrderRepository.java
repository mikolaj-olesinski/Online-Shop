package org.example.produktylist.Order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Metoda do znalezienia zamówień dla konkretnego użytkownika
    List<Order> findByUserId(Long userId);
}
