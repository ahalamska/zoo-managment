package com.domy.zoomanagement.repository;

import com.domy.zoomanagement.models.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalsRepository extends JpaRepository<Animal, Long> {
    List<Animal> findAnimalsById(Long animalId);

    Animal findAnimalBySpecies(String species);

    List<Animal> findAll();
}
