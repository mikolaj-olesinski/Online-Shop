package org.example.produktylist.Entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Klasa reprezentująca koszyk.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items;

    public BigDecimal getTotalPrice() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return items.stream()
                .map(item -> BigDecimal.valueOf(item.getProduct().getPrice())
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
