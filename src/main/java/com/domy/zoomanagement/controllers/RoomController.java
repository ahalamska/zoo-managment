package com.domy.zoomanagement.controllers;

import com.domy.zoomanagement.models.Animal;
import com.domy.zoomanagement.models.Room;
import com.domy.zoomanagement.models.Species;
import com.domy.zoomanagement.repository.AnimalsRepository;
import com.domy.zoomanagement.repository.RoomRepository;
import com.domy.zoomanagement.repository.SpeciesRepository;
import com.domy.zoomanagement.requests.RoomRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    public RoomController(AnimalsRepository animalsRepository, RoomRepository roomRepository, SpeciesRepository speciesRepository) {
        this.animalsRepository = animalsRepository;
        this.roomRepository = roomRepository;
        this.speciesRepository = speciesRepository;
    }

    AnimalsRepository animalsRepository;

    RoomRepository roomRepository;

    SpeciesRepository speciesRepository;

    @GetMapping(produces = {"application/json"})
    public @ResponseBody
    List<Room> getRooms() {
        return roomRepository.findAll();
    }

    @GetMapping(value = "/{roomId}")
    public @ResponseBody List<Animal> getAnimalsInRoom(@PathVariable Long roomId) {
        return animalsRepository.findAll().stream()
                .filter(animal -> animal.getRoom().getId().equals(roomId))
                .collect(Collectors.toList());
    }

    @PostMapping(consumes = "application/json")
    public Room createRoom(@RequestBody @Valid RoomRequest request) {
        Species species = speciesRepository.findByName(request.getSpeciesName())
                .orElseThrow(() -> new ResourceNotFoundException("Spiece " + request.getSpeciesName() + " not found"));
        Room room = Room.builder()
                .species(asList(species))
                .surface(request.getSurface())
                .price(request.getPrice())
                .locatorsMaxNumber(request.getLocatorsMaxNumber())
                .build();
        return roomRepository.save(room);
    }

    /*@PutMapping("/room/{roomId}")
    public Room updateCaretaker(@PathVariable Long roomId,
                               @Valid @RequestBody Long caretakerId) {
        return roomRepository.findById(roomId)
                .map(room -> {
                    room.setCaretakerId(caretakerId);
                    return roomRepository.save(room);
                }).orElseThrow(() -> new ResourceNotFoundException(("Room not found with given ID")));
    }*/

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long roomId) {
        return roomRepository.findById(roomId)
                .map(room -> {
                    roomRepository.delete(room);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Room not found with given ID"));
    }

}
