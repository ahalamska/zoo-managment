package com.domy.zoomanagement.controllers;

import com.domy.zoomanagement.models.Species;
import com.domy.zoomanagement.repository.SpeciesRepository;
import com.domy.zoomanagement.requests.SpeciesRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class SpeciesController {

    private final SpeciesRepository speciesRepository;

    @Autowired
    public SpeciesController(SpeciesRepository speciesRepository) {this.speciesRepository = speciesRepository;}

    @GetMapping(value = "/species", produces = {"application/json"})
    public @ResponseBody
    List<Species> getSpecies() {
        return speciesRepository.findAll();
    }

    @PostMapping(value = "/species", consumes = "application/json")
    public Species createSpecies(@Valid @RequestBody SpeciesRequest request) {
        Species species =
                Species.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .food(request.getFood())
                        .naturalHabitat(request.getNaturalHabitat())
                        .price(request.getPrice())
                        .build();
        return speciesRepository.save(species);
    }

    @PutMapping("/species/{speciesName}")
    public Species updateSpecies(@PathVariable String speciesName,
            @RequestBody SpeciesRequest request) {
        return speciesRepository.findByName(speciesName)
                .map(species -> {
                    if (request.getDescription() != null){
                        species.setDescription(request.getDescription());
                    }
                    if (request.getFood() != null){
                        species.setFood(request.getFood());
                    }
                    if (request.getNaturalHabitat() != null){
                        species.setNaturalHabitat(request.getNaturalHabitat());
                    }
                    if (request.getPrice() != null){
                        species.setPrice(request.getPrice());
                    }
                    return speciesRepository.save(species);
                }).orElseThrow(() -> new ResourceNotFoundException(("Species with given name not found")));
    }

    @DeleteMapping("/species/{speciesName}")
    public ResponseEntity deleteAnimal(@PathVariable String speciesName) {
        return speciesRepository.findByName(speciesName)
                .map(species -> {
                    speciesRepository.delete(species);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Animal not found with given ID"));
    }


}
