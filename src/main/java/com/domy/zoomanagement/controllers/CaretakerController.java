package com.domy.zoomanagement.controllers;

import com.domy.zoomanagement.managers.EmployeesManager;
import com.domy.zoomanagement.models.Caretaker;
import com.domy.zoomanagement.repository.CaretakerRepository;
import com.domy.zoomanagement.repository.ContractsRepository;
import com.domy.zoomanagement.repository.RoomRepository;
import com.domy.zoomanagement.requests.CaretakerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/caretakers")
public class CaretakerController {

    static final String CARETAKER_NOT_FOUND = "Caretaker not found with given ID";

    private CaretakerRepository caretakersRepository;

    private ContractsRepository contractsRepository;

    private RoomRepository roomRepository;

    private EmployeesManager caretakerManager;

    @Autowired
    public CaretakerController(CaretakerRepository caretakersRepository, ContractsRepository contractsRepository,
            RoomRepository roomRepository, EmployeesManager caretakerManager) {
        this.caretakersRepository = caretakersRepository;
        this.contractsRepository = contractsRepository;
        this.roomRepository = roomRepository;
        this.caretakerManager = caretakerManager;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(produces = {"application/json"})
    public @ResponseBody
    List<Caretaker> getCaretakers() {
        return caretakersRepository.findAll();
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/{caretakerId}",produces = {"application/json"})
    public @ResponseBody
    Caretaker getCaretaker(@PathVariable Long caretakerId) {
        return caretakersRepository.findById(caretakerId)
                .orElseThrow(() -> new ResourceNotFoundException(CARETAKER_NOT_FOUND));
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(consumes = "application/json")
    public Caretaker createCaretaker(@RequestBody @Valid CaretakerRequest request) {

        return caretakerManager.createCaretaker(request.getFirstName(), request.getName(),
                request.getType());
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{caretakerId}")
    public ResponseEntity deleteCaretaker(@PathVariable Long caretakerId) {
        roomRepository.findAllNotEmptyByCaretaker(caretakerId).ifPresent(list -> {
            throw new IllegalStateException("Cannot delete caretaker: there are rooms that need him!");
        });
        return caretakersRepository.findById(caretakerId).map(caretaker -> {
            caretakersRepository.delete(caretaker);
            contractsRepository.delete(caretaker.getContract());
            return ResponseEntity.ok().body("Deleted caretaker and his contract");
        }).orElseThrow(() -> new ResourceNotFoundException(CARETAKER_NOT_FOUND));
    }
}
