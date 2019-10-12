package com.domy.zoomanagement.controllers;

import com.domy.zoomanagement.models.Animal;
import com.domy.zoomanagement.repository.AnimalsRepository;
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

    @GetMapping(value = "/animals", produces = {"application/json"})
    public @ResponseBody List<Animal> getAnimals() {
        return animalsRepository.findAll();
    }

    @PostMapping(value = "/animals", consumes = "application/json")
    public Animal createAnimal(@RequestBody Animal animal) {
        return animalsRepository.save(animal);
    }

    @PutMapping("/animals/{animalId}")
    public Animal updateAnimal(@PathVariable Long animalId,
                               @Valid @RequestBody Animal animalRequest) {
        return animalsRepository.findById(animalId)
                .map(animal -> {
//                    if(animalRequest.getRoom() != null) animal.setRoom(animalRequest.getRoom());
                    if(animalRequest.getSpecies() != null) animal.setSpecies(animalRequest.getSpecies());
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
