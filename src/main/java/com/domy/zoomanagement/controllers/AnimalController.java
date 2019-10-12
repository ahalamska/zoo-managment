package com.domy.zoomanagement.controllers;

import com.domy.zoomanagement.models.Animal;
import com.domy.zoomanagement.repository.AnimalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static java.util.Arrays.asList;

@Controller
@RequestMapping
public class AnimalController {

    @Autowired
    private AnimalsRepository animalsRepository;

    @GetMapping(value = "/animals", produces = "application/json")
    public List<Animal> getAnimals() {
        return asList(Animal.builder().species("Slon").build());
    }
}
