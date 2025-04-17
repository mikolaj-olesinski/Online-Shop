package org.example.produktylist.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Klasa reprezentująca kategorię produktów.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();


}
