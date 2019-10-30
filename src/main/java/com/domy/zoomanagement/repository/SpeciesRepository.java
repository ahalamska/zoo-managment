package com.domy.zoomanagement.repository;

import com.domy.zoomanagement.models.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpeciesRepository extends JpaRepository<Species, Long> {

    List<Species> findAll();

    Optional<Species> findByName(String name);

    Optional<List<Species>> findAllByName(List<String> names);
}
