package org.example.produktylist.Product;

import lombok.*;
import org.example.produktylist.Category.Category;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private double price;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Nowe pola:
    private String image1;
    private String image2;
    private String image3;
    private String image4;

    private String description1;
    private String description2;
}
