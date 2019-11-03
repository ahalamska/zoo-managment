package com.domy.zoomanagement.controllers;

import com.domy.zoomanagement.models.Enclosure;
import com.domy.zoomanagement.repository.EnclosureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enclosures")
public class EnclosureController {

    static final String ENCLOSURE_NOT_FOUND = "Enclosure with given ID wasn't found";

    private EnclosureRepository enclosureRepository;

    @Autowired
    public EnclosureController(EnclosureRepository enclosureRepository) {
        this.enclosureRepository = enclosureRepository;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(produces = {"application/json"})
    public @ResponseBody
    List<Enclosure> getEnclosures() {
        return enclosureRepository.findAll();
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/{enclosureId}")
    public @ResponseBody
    Enclosure getEnclosureInfo(@PathVariable Long enclosureId) {
        return enclosureRepository.findById(enclosureId).orElseThrow(() -> new ResourceNotFoundException(ENCLOSURE_NOT_FOUND));
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PatchMapping("{enclosureId}/buy")
    public Enclosure buyEnclosure(@PathVariable Long enclosureId) {
        return enclosureRepository.findById(enclosureId).map(enclosure -> {
            if (enclosure.isBought()) throw new IllegalStateException("Enclosure already bought");
            enclosure.setBought(true);
            //TODO count budget
            return enclosureRepository.save(enclosure);
        }).orElseThrow(() -> new ResourceNotFoundException((ENCLOSURE_NOT_FOUND)));
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PatchMapping("/{enclosureId}/destroy")
    public Enclosure destroyEnclosure(@PathVariable Long enclosureId) {
        return enclosureRepository.findById(enclosureId).map(enclosure -> {
            if (!enclosure.isBought()) throw new IllegalStateException("Enclosure is not bought");
            enclosure.setBought(false);
            return enclosureRepository.save(enclosure);
        }).orElseThrow(() -> new ResourceNotFoundException((ENCLOSURE_NOT_FOUND)));
    }
}
