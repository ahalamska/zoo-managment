package com.domy.zoomanagement.controllers;

import com.domy.zoomanagement.models.*;
import com.domy.zoomanagement.repository.*;
import com.domy.zoomanagement.requests.RoomRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    public static final String ROOM_NOT_FOUND = "Room with given ID wasn't found";
    AnimalsRepository animalsRepository;
    RoomRepository roomRepository;
    SpeciesRepository speciesRepository;
    CaretakerRepository caretakerRepository;
    EnclosuresRepository enclosuresRepository;

    @Autowired
    public RoomController(AnimalsRepository animalsRepository, RoomRepository roomRepository,
            SpeciesRepository speciesRepository, CaretakerRepository caretakerRepository,
            EnclosuresRepository enclosuresRepository) {
        this.animalsRepository = animalsRepository;
        this.roomRepository = roomRepository;
        this.speciesRepository = speciesRepository;
        this.caretakerRepository = caretakerRepository;
        this.enclosuresRepository = enclosuresRepository;
    }

    @GetMapping(produces = {"application/json"})
    public @ResponseBody
    List<Room> getRooms() {
        return roomRepository.findAll();
    }

    @GetMapping(value = "/{roomId}/animals")
    public @ResponseBody
    List<Animal> getAnimalsInRoom(@PathVariable Long roomId) {
        return animalsRepository.findAllByRoom(roomId).orElse(null);
    }

    @GetMapping(value = "/{roomId}")
    public @ResponseBody
    Room getRoomInfo(@PathVariable Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));
    }

    @PostMapping(consumes = "application/json")
    public Room createRoom(@RequestBody @Valid RoomRequest request) {
        List<Species> species = speciesRepository.findAllByName(request.getSpeciesNames())
                .orElseThrow(() -> new ResourceNotFoundException("None of given species were found"));
        if (species.size() != request.getSpeciesNames().size()) {
            throw new ResourceNotFoundException("One of given species were not found");
        }
        Enclosure enclosure = null;
        Caretaker caretaker = null;
        if (request.getEnclosureId() != null) {
            enclosure = enclosuresRepository.findById(request.getEnclosureId())
                    .orElseThrow(() -> new ResourceNotFoundException("Enclosure not found with given ID"));
        }
        if (request.getCaretakerId() != null) {
             caretaker = caretakerRepository.findById(request.getCaretakerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Caretaker not found with given ID"));
        }

        Room room = Room.builder()
                .species(species)
                .surface(request.getSurface())
                .price(request.getPrice())
                .locatorsMaxNumber(request.getLocatorsMaxNumber())
                .localization(request.getLocalization())
                .enclosure(enclosure)
                .caretaker(caretaker)
                .build();

        return roomRepository.save(room);
    }

    @PatchMapping("{roomId}/caretaker")
    public Room updateCaretaker(@PathVariable Long roomId, @RequestBody Long caretakerId) {
        Caretaker caretaker = caretakerRepository.findById(caretakerId)
                .orElseThrow(() -> new IllegalArgumentException(("Caretaker not found with given ID")));
        return roomRepository.findById(roomId).map(room -> {
            room.setCaretaker(caretaker);
            return roomRepository.save(room);
        }).orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));
    }

    @PatchMapping("{roomId}/buy")
    public Room buyRoom(@PathVariable Long roomId) {
        return roomRepository.findById(roomId).map(room -> {
            if (room.isBought()) throw new IllegalStateException("Room already bought");
            room.setBought(true);
            //TODO count budget
            return roomRepository.save(room);
        }).orElseThrow(() -> new ResourceNotFoundException((ROOM_NOT_FOUND)));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long roomId) {
        return roomRepository.findById(roomId).map(room -> {
            animalsRepository.findAllByRoom(roomId)
                    .ifPresent(animals -> {throw new IllegalStateException("There are animals in this room!");});
            roomRepository.delete(room);
            return ResponseEntity.ok().body("deleted room: " + roomId.toString());
        }).orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));
    }

}
