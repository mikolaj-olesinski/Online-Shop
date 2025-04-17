package org.example.produktylist.Entity;

import lombok.*;
import javax.persistence.*;

/**
 * Klasa reprezentująca formularz danych.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "data_forms") // Tabela dla formularza danych
public class DataForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String company;
    private String address;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String deliveryType;
    private String paymentType;
    private String notes;
}
