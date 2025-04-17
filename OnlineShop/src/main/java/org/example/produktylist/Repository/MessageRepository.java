package org.example.produktylist.Repository;

import org.example.produktylist.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByOrderId(Long orderId);
}
