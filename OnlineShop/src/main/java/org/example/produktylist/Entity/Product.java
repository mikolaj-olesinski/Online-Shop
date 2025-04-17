package org.example.produktylist.Entity;

import lombok.*;

import javax.persistence.*;

/**
 * Klasa reprezentująca produkt.
 */
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

    private String image1;
    private String image2;
    private String image3;
    private String image4;

    private String description1;
    private String description2;
}
