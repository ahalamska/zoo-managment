package com.domy.zoomanagement.managers;

import com.domy.zoomanagement.models.Caretaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CaretakersManager {

    private ContractManager contractManager;

    @Autowired
    public CaretakersManager(ContractManager contractManager) {
        this.contractManager = contractManager;
    }

    public Caretaker createCaretaker(String firstName, String name, CARETAKER_TYPE type){
        return Caretaker.builder()
                .contract(contractManager.createCaretakerContract(type))
                .name(name)
                .firstName(firstName)
                .roomMaxNumber(getRoomMaxNumber(type))
                .build();
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

    public enum CARETAKER_TYPE {
        JUNIOR, REGULAR, SENIOR, LEAD
    }
}

