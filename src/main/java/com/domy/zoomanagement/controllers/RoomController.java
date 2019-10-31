package com.domy.zoomanagement.controllers;

import com.domy.zoomanagement.models.Animal;
import com.domy.zoomanagement.models.Caretaker;
import com.domy.zoomanagement.models.Enclosure;
import com.domy.zoomanagement.models.Room;
import com.domy.zoomanagement.repository.AnimalsRepository;
import com.domy.zoomanagement.repository.CaretakerRepository;
import com.domy.zoomanagement.repository.EnclosureRepository;
import com.domy.zoomanagement.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.domy.zoomanagement.controllers.CaretakerController.CARETAKER_NOT_FOUND;
import static com.domy.zoomanagement.controllers.EnclosureController.ENCLOSURE_NOT_FOUND;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    static final String ROOM_NOT_FOUND = "Room with given ID wasn't found";
    private AnimalsRepository animalsRepository;
    private RoomRepository roomRepository;
    private CaretakerRepository caretakerRepository;
    private EnclosureRepository enclosureRepository;

    @Autowired
    public RoomController(AnimalsRepository animalsRepository, RoomRepository roomRepository,
            CaretakerRepository caretakerRepository, EnclosureRepository enclosureRepository) {
        this.animalsRepository = animalsRepository;
        this.roomRepository = roomRepository;
        this.caretakerRepository = caretakerRepository;
        this.enclosureRepository = enclosureRepository;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(produces = {"application/json"})
    public @ResponseBody
    List<Room> getRooms() {
        return roomRepository.findAll();
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/{roomId}/animals")
    public @ResponseBody
    List<Animal> getAnimalsInRoom(@PathVariable Long roomId) {
        return animalsRepository.findAllByRoom(roomId).orElse(null);
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/{roomId}")
    public @ResponseBody
    Room getRoomInfo(@PathVariable Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PatchMapping("{roomId}/enclosure")
    public Room updateEnclosure(@PathVariable Long roomId, @RequestBody Long enclosureId) {

        Enclosure enclosure = enclosureRepository.findById(enclosureId)
                .orElseThrow(() -> new IllegalArgumentException((ENCLOSURE_NOT_FOUND)));

        if (!enclosure.isBought()) throw new IllegalStateException("Enclosure is not bought!");

        return roomRepository.findById(roomId).map(room -> {
            room.setEnclosure(enclosure);
            return roomRepository.save(room);
        }).orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PatchMapping("{roomId}/caretaker")
    public Room updateCaretaker(@PathVariable Long roomId, @RequestBody Long caretakerId) {

        Caretaker caretaker = caretakerRepository.findById(caretakerId)
                .orElseThrow(() -> new IllegalArgumentException((CARETAKER_NOT_FOUND)));

        return roomRepository.findById(roomId).map(room -> {
            room.setCaretaker(caretaker);
            return roomRepository.save(room);
        }).orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PatchMapping("{roomId}/buy")
    public Room buyRoom(@PathVariable Long roomId, @RequestBody String localization) {
        return roomRepository.findById(roomId).map(room -> {
            if (room.isBought()) throw new IllegalStateException("Room already bought");
            room.setBought(true);
            room.setLocalization(localization);
            //TODO count budget
            return roomRepository.save(room);
        }).orElseThrow(() -> new ResourceNotFoundException((ROOM_NOT_FOUND)));
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PatchMapping("/{roomId}/destroy")
    public Room deleteRoom(@PathVariable Long roomId) {
        return roomRepository.findById(roomId).map(room -> {
            if (!room.isBought()) throw new IllegalStateException("Room is not bought");
            animalsRepository.findAllByRoom(roomId)
                    .ifPresent(animals -> {throw new IllegalStateException("There are animals in this room!");});
            room.setBought(false);
            room.setCaretaker(null);
            room.setEnclosure(null);
            return roomRepository.save(room);
        }).orElseThrow(() -> new ResourceNotFoundException((ROOM_NOT_FOUND)));
    }
}
