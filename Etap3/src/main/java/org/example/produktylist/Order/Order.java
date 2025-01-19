package org.example.produktylist.Order;

import lombok.*;
import org.example.produktylist.Cart.Cart;
import org.example.produktylist.User.User;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")  // Zmiana nazwy tabeli na "orders"
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Powiązanie z użytkownikiem

    @OneToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart; // Powiązanie z koszykiem

    // Możesz dodać inne atrybuty, jak data zamówienia, status, itp.
} 