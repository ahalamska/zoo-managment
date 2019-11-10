package com.domy.zoomanagement.repository;

import com.domy.zoomanagement.models.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalsRepository extends JpaRepository<Animal, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM animals a WHERE a.room_id = ?1")
    Optional<List<Animal>> findAllByRoom(Long roomId);

    @Query(nativeQuery = true, value = "select SUM(species.prestige_points) FROM animals LEFT JOIN species ON animals" +
            ".species_name = species.name")
    Integer getSumOfPrestigePoints();

    List<Animal> findAll();
}
