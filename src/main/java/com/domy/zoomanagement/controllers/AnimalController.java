package com.domy.zoomanagement.controllers;

import com.domy.zoomanagement.models.Animal;
import com.domy.zoomanagement.models.Room;
import com.domy.zoomanagement.models.Species;
import com.domy.zoomanagement.repository.AnimalsRepository;
import com.domy.zoomanagement.repository.RoomRepository;
import com.domy.zoomanagement.repository.SpeciesRepository;
import com.domy.zoomanagement.requests.AnimalRequest;
import com.domy.zoomanagement.utils.OptionalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AnimalController {

    private final AnimalsRepository animalsRepository;

    private final SpeciesRepository speciesRepository;

    private final RoomRepository roomRepository;

    @Autowired
    public AnimalController(AnimalsRepository animalsRepository, SpeciesRepository speciesRepository, RoomRepository roomRepository) {
        this.animalsRepository = animalsRepository;
        this.speciesRepository = speciesRepository;
        this.roomRepository = roomRepository;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/animals", produces = {"application/json"})
    public @ResponseBody
    List<Animal> getAnimals() {
        return animalsRepository.findAll();
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "/animals", consumes = "application/json")
    public Animal createAnimal(@Valid @RequestBody AnimalRequest request) {
        Species species =
                speciesRepository.findByName(request.getSpecies())
                        .orElseThrow(() -> new ResourceNotFoundException(("Species with given name wasn't found")));

        Room room =
                roomRepository.findById(request.getRoom())
                        .orElseThrow(() -> new ResourceNotFoundException(("Room not found with given ID")));

        Animal animal = Animal.builder().name(OptionalUtil.handleNullable(request.getName()))
                .species(species)
                .room(room)
                .build();

        return animalsRepository.save(animal);
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/animals/{animalId}")
    public Animal updateAnimal(@PathVariable Long animalId,
            @Valid @RequestBody AnimalRequest request) {
        return animalsRepository.findById(animalId)
                .map(animal -> {
                    animal.setRoom(roomRepository.findById((request.getRoom())).orElseThrow(() -> new ResourceNotFoundException(("Cannot set room that doesn't exist!"))));
                    if (request.getName() != null) animal.setName(request.getName());
                    if (request.getSpecies() != null){
                        animal.setSpecies(speciesRepository.findByName(request.getSpecies()).orElseThrow(() -> new ResourceNotFoundException((
                                "Species with given name wasn't found"))));
                    }
                    return animalsRepository.save(animal);
                }).orElseThrow(() -> new ResourceNotFoundException(("Animal with given ID not found")));
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/animals/{animalId}")
    public ResponseEntity<?> deleteAnimal(@PathVariable Long animalId) {
        return animalsRepository.findById(animalId)
                .map(animal -> {
                    animalsRepository.delete(animal);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Animal not found with given ID"));
    }


}
