package com.domy.zoomanagement.repository;

import com.domy.zoomanagement.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM room r  LEFT JOIN animals a ON r.id = a.room_id" +
            " WHERE r.caretaker_id = ?1")
    Optional<List<Room>> findAllNotEmptyByCaretaker(Long caretakerId);

    @Query(nativeQuery = true, value = "UPDATE room SET bought = false WHERE bought = true")
    void resetRooms();
}
