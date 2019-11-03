package com.domy.zoomanagement.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Budget {

    @Id
    @Column(nullable = false, unique = true)
    private LocalDate roundDate;

    @Column(nullable = false)
    private Integer happinessRate;

    @Column(nullable = false)
    private Float ticketPrice = 30f;

    @Column(nullable = false)
    private Float availableFunds;

    @Column(nullable = false)
    private Float euFunds;

    @Column(nullable = false)
    private Float stateBudgetFunds;

    public void addMoney(float amount) {
        availableFunds += amount;
    }
    public void subtractMoney(float amount) {
        availableFunds -= amount;
    }
}
