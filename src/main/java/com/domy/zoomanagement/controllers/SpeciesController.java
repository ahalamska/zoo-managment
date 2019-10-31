package com.domy.zoomanagement.controllers;

import com.domy.zoomanagement.models.Species;
import com.domy.zoomanagement.repository.SpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/species")
public class SpeciesController {

    static final String SPECIES_NOT_FOUND = "Species with given name wasn't found";
    private SpeciesRepository speciesRepository;

    @Autowired
    public SpeciesController(SpeciesRepository speciesRepository) {this.speciesRepository = speciesRepository;}


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(produces = {"application/json"})
    public @ResponseBody
    List<Species> getAllSpecies() {
        return speciesRepository.findAll();
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/{name}", produces = {"application/json"})
    public @ResponseBody
    Species getSpecies(@PathVariable String name) {
        return speciesRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException(SPECIES_NOT_FOUND));
    }
}
