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

    public static final String SPECIES_NOT_FOUND = "Species with given name wasn't found";
    private SpeciesRepository speciesRepository;

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
                        .photoUrl(request.getPhotoUrl())
                        .food(request.getFood())
                        .naturalHabitat(request.getNaturalHabitat())
                        .price(request.getPrice())
                        .build();
        return speciesRepository.save(species);
    }

    @PatchMapping("/species/{speciesName}")
    public Species updateSpecies(@PathVariable String speciesName,
            @RequestBody SpeciesRequest request) {
        return speciesRepository.findByName(speciesName)
                .map(species -> {
                    if (request.getDescription() != null){
                        species.setDescription(request.getDescription());
                    }
                    if (request.getPhotoUrl() != null){
                        species.setPhotoUrl(request.getPhotoUrl());
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
                }).orElseThrow(() -> new ResourceNotFoundException(SPECIES_NOT_FOUND));
    }

    @DeleteMapping("/species/{speciesName}")
    public ResponseEntity deleteSpecies(@PathVariable String speciesName) {
        return speciesRepository.findByName(speciesName)
                .map(species -> {
                    speciesRepository.delete(species);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException(SPECIES_NOT_FOUND));
    }


}
