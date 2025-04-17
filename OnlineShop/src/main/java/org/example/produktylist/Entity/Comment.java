package org.example.produktylist.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Klasa reprezentująca komentarz.
 */
@Entity
@Data
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(length = 1000)
    private String content;

    private int rating;
}
