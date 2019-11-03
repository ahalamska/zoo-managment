package com.domy.zoomanagement.managers;

import com.domy.zoomanagement.models.Caretaker;
import com.domy.zoomanagement.models.Entertainer;
import com.domy.zoomanagement.repository.CaretakerRepository;
import com.domy.zoomanagement.repository.ContractsRepository;
import com.domy.zoomanagement.repository.EntertainersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeesManager {

    private ContractBuilder contractBuilder;
    private ContractsRepository contractsRepository;
    private EntertainersRepository entertainersRepository;
    private CaretakerRepository caretakersRepository;

    @Autowired
    public EmployeesManager(ContractBuilder contractBuilder, ContractsRepository contractsRepository, EntertainersRepository entertainersRepository, CaretakerRepository caretakersRepository) {
        this.contractBuilder = contractBuilder;
        this.contractsRepository = contractsRepository;
        this.entertainersRepository = entertainersRepository;
        this.caretakersRepository = caretakersRepository;
    }

    public Caretaker createCaretaker(String firstName, String name, CARETAKER_TYPE type){
        Caretaker caretaker = Caretaker.builder()
                .contract(contractBuilder.createCaretakerContract(type))
                .name(name)
                .firstName(firstName)
                .roomMaxNumber(getRoomMaxNumber(type))
                .build();

        contractsRepository.save(caretaker.getContract());
        caretakersRepository.save(caretaker);
        return caretaker;
    }

    private Integer getRoomMaxNumber(CARETAKER_TYPE type){
        switch (type){
            case JUNIOR:
                return 3;
            case REGULAR:
                return 7;
            case SENIOR:
                return 12;
            case LEAD:
                return 20;
        }
        throw new IllegalStateException("Given caretaker type doesn't exist");
    }

    public Entertainer createEntertainer(String firstName, String name) {
        Entertainer entertainer = Entertainer.builder()
                .firstName(firstName)
                .name(name)
                .contract(contractBuilder.createEntertainerContract())
                .build();

        contractsRepository.save(entertainer.getContract());

        entertainersRepository.save(entertainer);

        return entertainer;
    }

    public void fireEmployees() {
        caretakersRepository.deleteAll();
        entertainersRepository.deleteAll();
        contractsRepository.deleteAll();
    }

    public enum CARETAKER_TYPE {
        JUNIOR, REGULAR, SENIOR, LEAD
    }
}

