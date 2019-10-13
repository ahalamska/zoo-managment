package com.domy.zoomanagement.controllers;

import com.domy.zoomanagement.models.Animal;
import com.domy.zoomanagement.repository.AnimalsRepository;
import com.domy.zoomanagement.repository.RoomRepository;
import com.domy.zoomanagement.utils.OptionalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AnimalController {

    @Autowired
    private AnimalsRepository animalsRepository;
    @Autowired
    RoomRepository roomRepository;

    @GetMapping(value = "/animals", produces = {"application/json"})
    public @ResponseBody
    List<Animal> getAnimals() {
        return animalsRepository.findAll();
    }

    @PostMapping(value = "/animals", consumes = "application/json")
    public Animal createAnimal(@Valid @RequestBody AnimalRequest request) {
        Animal animal = Animal.builder()
                .name(OptionalUtil.handleNullable(request.getName()))
                .species(OptionalUtil.handleNullable(request.getSpecies()))
                .room(roomRepository.findById(request.getRoom()).orElseThrow(() -> new ResourceNotFoundException(("Room not found with given ID"))))
                .build();
        return animalsRepository.save(animal);
    }

    @PutMapping("/animals/{animalId}")
    public Animal updateAnimal(@PathVariable Long animalId,
                               @Valid @RequestBody AnimalRequest request) {
        return animalsRepository.findById(animalId)
                .map(animal -> {
                    animal.setRoom(roomRepository.findById((request.getRoom())).orElseThrow(() -> new ResourceNotFoundException(("Cannot set room that doesn't exist!"))));
                    if (request.getName() != null) animal.setName(request.getName());
                    if (request.getSpecies() != null) animal.setSpecies(request.getSpecies());
                    return animalsRepository.save(animal);
                }).orElseThrow(() -> new ResourceNotFoundException(("Animal not found with given ID")));
    }

    @DeleteMapping("/animals/{animalId}")
    public ResponseEntity<?> deleteAnimal(@PathVariable Long animalId) {
        return animalsRepository.findById(animalId)
                .map(animal -> {
                    animalsRepository.delete(animal);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Animal not found with given ID"));
    }


}
