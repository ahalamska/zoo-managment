package com.domy.zoomanagement.controllers;

import com.domy.zoomanagement.managers.ContractManager;
import com.domy.zoomanagement.models.Entertainer;
import com.domy.zoomanagement.repository.ContractsRepository;
import com.domy.zoomanagement.repository.EntertainersRepository;
import com.domy.zoomanagement.requests.EntertainerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/entertainers")
public class EntertainerController {

    public static final String ENTERTAINER_NOT_FOUND = "Entertainer not found with given ID";

    private EntertainersRepository entertainersRepository;

    private ContractsRepository contractsRepository;

    private ContractManager contractManager;

    @Autowired
    public EntertainerController(EntertainersRepository entertainersRepository, ContractsRepository contractsRepository, ContractManager contractManager) {
        this.entertainersRepository = entertainersRepository;
        this.contractsRepository = contractsRepository;
        this.contractManager = contractManager;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(produces = {"application/json"})
    public @ResponseBody
    List<Entertainer> getEntertainers() {
        return entertainersRepository.findAll();
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/{entertainerId}",produces = {"application/json"})
    public @ResponseBody
    Entertainer getEntertainer(@PathVariable Long entertainerId) {
        return entertainersRepository.findById(entertainerId)
                .orElseThrow(() -> new ResourceNotFoundException(ENTERTAINER_NOT_FOUND));
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(consumes = "application/json")
    public Entertainer createEntertainer(@RequestBody @Valid EntertainerRequest request) {

        Entertainer entertainer = Entertainer.builder()
                .firstName(request.getFirstName())
                .name(request.getName())
                .contract(contractManager.createEntertainerContract())
                .build();

        contractsRepository.save(entertainer.getContract());

        return entertainersRepository.save(entertainer);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{entertainerId}")
    public ResponseEntity<?> deleteEntertainer(@PathVariable Long entertainerId) {
        return entertainersRepository.findById(entertainerId).map(entertainer -> {
            entertainersRepository.delete(entertainer);
            contractsRepository.delete(entertainer.getContract());
            return ResponseEntity.ok().body("Deleted entertainer and his contract");
        }).orElseThrow(() -> new ResourceNotFoundException(ENTERTAINER_NOT_FOUND));
    }
}