package com.domy.zoomanagement.controllers;

import com.domy.zoomanagement.models.Caretaker;
import com.domy.zoomanagement.models.Contract;
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

    private CaretakerRepository caretakersRepository;

    private ContractsRepository contractsRepository;

    private RoomRepository roomRepository;

    @Autowired
    public CaretakerController(CaretakerRepository caretakersRepository, ContractsRepository contractsRepository,
            RoomRepository roomRepository) {
        this.caretakersRepository = caretakersRepository;
        this.contractsRepository = contractsRepository;
        this.roomRepository = roomRepository;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(produces = {"application/json"})
    public @ResponseBody
    List<Caretaker> getCaretakers() {
        return caretakersRepository.findAll();
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(consumes = "application/json")
    public Caretaker createCaretaker(@Valid @RequestBody CaretakerRequest request) {

        Contract contract = contractsRepository.findById(request.getContractId())
                .orElseThrow(() -> new ResourceNotFoundException("Contract with given ID wasn't found"));

        Caretaker caretaker = Caretaker.builder()
                .firstName(request.getFirstName())
                .name(request.getName())
                .roomMaxNumber(request.getRoomMaxNumber())
                .contract(contract)
                .build();

        return caretakersRepository.save(caretaker);
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PatchMapping("/{caretakerId}")
    public Caretaker updateCaretaker(@PathVariable Long caretakerId, @RequestBody CaretakerRequest request) {
        return caretakersRepository.findById(caretakerId).map(caretaker -> {
            if (request.getRoomMaxNumber() != null) caretaker.setRoomMaxNumber(request.getRoomMaxNumber());
            if (request.getContractId() != null) {
                caretaker.setContract(contractsRepository.findById(request.getContractId())
                        .orElseThrow(() -> new ResourceNotFoundException(("Cannot update caretaker : Contract with " + "given ID " + "wasn't found"))));
            }
            return caretakersRepository.save(caretaker);
        }).orElseThrow(() -> new ResourceNotFoundException(("Caretaker with given ID not found")));
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{caretakerId}")
    public ResponseEntity<?> deleteCaretaker(@PathVariable Long caretakerId) {
        return caretakersRepository.findById(caretakerId).map(caretaker -> {
            caretakersRepository.delete(caretaker);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Caretaker not found with given ID"));
    }


}
