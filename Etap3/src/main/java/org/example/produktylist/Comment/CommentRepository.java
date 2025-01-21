package org.example.produktylist.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Metoda do pobierania komentarzy dla konkretnego produktu
    List<Comment> findByProductId(Long productId);
}
