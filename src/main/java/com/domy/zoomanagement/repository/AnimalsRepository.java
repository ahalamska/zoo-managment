package com.domy.zoomanagement.repository;

import com.domy.zoomanagement.models.Animal;
import com.domy.zoomanagement.models.Room;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalsRepository extends JpaRepository<Animal, Long> {
    List<Animal> findAnimalsById(Long animalId);

    @Query(nativeQuery = true,
            value = "SELECT * FROM animals a WHERE a.room_id = ?1")
    Optional<List<Animal>> findAllByRoom(Long roomId);

    List<Animal> findAll();
}
