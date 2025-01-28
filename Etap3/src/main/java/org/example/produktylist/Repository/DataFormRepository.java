package org.example.produktylist.Repository;

import org.example.produktylist.Entity.DataForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataFormRepository extends JpaRepository<DataForm, Long> {
}