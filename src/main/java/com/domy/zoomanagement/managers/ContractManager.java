package com.domy.zoomanagement.managers;

import com.domy.zoomanagement.managers.CaretakersManager.CARETAKER_TYPE;
import com.domy.zoomanagement.models.Contract;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import static java.lang.Math.round;

@Component
public class ContractManager {

    Contract createCaretakerContract(CARETAKER_TYPE type){
        return Contract.builder()
                .payment(getPayment(type))
                .signingDate(new Date())
                .build();
    }

    private Float getPayment(CARETAKER_TYPE type){
        Random random = new SecureRandom();
        float extraPaymentFactor = random.nextFloat();
        switch (type){
            case JUNIOR:
                return  ((float) round((extraPaymentFactor * 750 + 2250) * 100) / 100);
            case REGULAR:
                return  ((float) round((extraPaymentFactor * 1000 + 3500) * 100) / 100);
            case SENIOR:
                return ((float) round((extraPaymentFactor * 2000 + 5000) * 100) / 100);
            case LEAD:
                return ((float) round((extraPaymentFactor * 4000 + 8000) * 100) / 100);
        }
        throw new IllegalStateException("Given caretaker type doesn't exist");
    }


    public Contract createEntertainerContract() {
        return Contract.builder()
                .payment(4000f)
                .signingDate(new Date())
                .build();
    }
}
