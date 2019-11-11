package com.domy.zoomanagement.repository;

import com.domy.zoomanagement.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM room r  LEFT JOIN animals a ON r.id = a.room_id" +
            " WHERE r.caretaker_id = ?1")
    Optional<List<Room>> findAllNotEmptyByCaretaker(Long caretakerId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE room SET bought = false, caretaker_id = null, enclosure_id = null")
    void resetRooms();


    @Query(nativeQuery = true,
    value = "SELECT r.id, r.bought, r.localization, r.locators_max_number, r.price, r.surface, r.caretaker_id, r.enclosure_id " +
            "FROM room r join room_species rs on r.id = rs.room_id " +
            "WHERE rs.species_name = ?1 AND r.bought is true")
    Optional<List<Room>> findAvailableForSpecies(String speciesName);

    @Query(nativeQuery = true,
            value = "SELECT count(*) as places FROM room r inner join animals a on a.room_id = r.id WHERE r.id = 7")
    Integer getNumberOfOccurrencesPlaces(Long roomId);

    @Query(nativeQuery = true,
            value = "SELECT * FROM room r WHERE r.bought = ?1 ")
    List<Room> findAll(boolean bought);
}
