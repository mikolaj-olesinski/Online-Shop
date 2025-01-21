package org.example.produktylist.Message;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.produktylist.Order.Order;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Date date;

    @Column(length = 1000)
    private String content;

}
