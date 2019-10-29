package com.domy.zoomanagement.repository;

import com.domy.zoomanagement.models.Caretaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaretakerRepository extends JpaRepository<Caretaker, Long> {

}
