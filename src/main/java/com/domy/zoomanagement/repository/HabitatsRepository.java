package com.domy.zoomanagement.repository;

import com.domy.zoomanagement.models.Habitat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitatsRepository extends JpaRepository<Habitat, Long> {

    List<Habitat> findAll();
}
