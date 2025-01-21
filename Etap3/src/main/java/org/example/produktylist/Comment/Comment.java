package org.example.produktylist.Comment;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.produktylist.Product.Product;
import org.example.produktylist.User.User;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Relacja z użytkownikiem, zakładając, że masz klasę User

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; // Relacja z produktem, zakładając, że masz klasę Product

    @Column(length = 1000)
    private String content; // Treść komentarza

    private int rating; // Ocena produktu
}
