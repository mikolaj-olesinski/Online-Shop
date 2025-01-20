package org.example.produktylist.Order;

import lombok.*;
import org.example.produktylist.Cart.Cart;
import org.example.produktylist.DataForm.DataForm;
import org.example.produktylist.User.User;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")  // Tabela dla zamówień
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart; // Powiązanie z koszykiem

    @OneToOne(cascade = CascadeType.ALL) // Kaskadowa operacja na formularzu danych
    @JoinColumn(name = "data_form_id", nullable = false)
    private DataForm dataForm;

    private String deliveryDate;
    private String status;
    private String statusDate;
}
