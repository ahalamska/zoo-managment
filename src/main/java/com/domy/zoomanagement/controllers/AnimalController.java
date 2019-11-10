package com.domy.zoomanagement.controllers;

import com.domy.zoomanagement.managers.BudgetManager;
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

import static com.domy.zoomanagement.controllers.RoomController.ROOM_NOT_FOUND;
import static com.domy.zoomanagement.controllers.SpeciesController.SPECIES_NOT_FOUND;

@RestController
@RequestMapping("/animals")
public class AnimalController {

    private static final String ANIMAL_NOT_FOUND = "Animal not found with given ID";

    private final AnimalsRepository animalsRepository;

    private final SpeciesRepository speciesRepository;

    private final RoomRepository roomRepository;

    private BudgetManager budgetManager;

    @Autowired
    public AnimalController(AnimalsRepository animalsRepository, SpeciesRepository speciesRepository, RoomRepository roomRepository, BudgetManager budgetManager) {
        this.animalsRepository = animalsRepository;
        this.speciesRepository = speciesRepository;
        this.roomRepository = roomRepository;
        this.budgetManager = budgetManager;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(produces = {"application/json"})
    public @ResponseBody
    List<Animal> getAnimals() {
        return animalsRepository.findAll();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/{id}", produces = {"application/json"})
    public @ResponseBody
    Animal getAnimals(@PathVariable Long id) {
        return animalsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ANIMAL_NOT_FOUND));
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(consumes = "application/json")
    public Animal buyAnimal(@Valid @RequestBody AnimalRequest request) {
        Species species =
                speciesRepository.findByName(request.getSpecies())
                        .orElseThrow(() -> new ResourceNotFoundException((SPECIES_NOT_FOUND)));
        Room room =
                roomRepository.findById(request.getRoom())
                        .orElseThrow(() -> new ResourceNotFoundException((ROOM_NOT_FOUND)));
        if(!room.isBought()) throw new IllegalStateException("Given room is not bought!");
        if(room.getCaretaker() == null) throw new IllegalStateException("Given room has no caretaker!");

        Animal animal = Animal.builder().name(OptionalUtil.handleNullable(request.getName()))
                .species(species)
                .room(room)
                .build();

        animalsRepository.save(animal);
        budgetManager.buy(species.getPrice());
        return animal;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PatchMapping("/{animalId}")
    public Animal updateAnimal(@PathVariable Long animalId, @RequestBody AnimalRequest request) {
        return animalsRepository.findById(animalId)
                .map(animal -> {
                    if (request.getName() != null) animal.setName(request.getName());
                    if (request.getRoom() != null){
                        animal.setRoom(roomRepository.findById(request.getRoom())
                                .orElseThrow(() -> new ResourceNotFoundException((
                                "Cannot update animal : " + ROOM_NOT_FOUND))));
                    }
                    return animalsRepository.save(animal);
                }).orElseThrow(() -> new ResourceNotFoundException((ANIMAL_NOT_FOUND)));
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{animalId}")
    public ResponseEntity sellAnimal(@PathVariable Long animalId) {
        return animalsRepository.findById(animalId)
                .map(animal -> {
                    animalsRepository.delete(animal);
                    budgetManager.sell(animal.getSpecies().getPrice()/2);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException(ANIMAL_NOT_FOUND));
    }


}
