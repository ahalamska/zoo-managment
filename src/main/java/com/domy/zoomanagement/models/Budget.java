package com.domy.zoomanagement.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Budget {

    @Id
    @Column(nullable = false, unique = true)
    private Date roundDate;

    @Column(nullable = false)
    private Float availableFunds;

    @Column(nullable = false)
    private Float euFunds;

    @Column(nullable = false)
    private Float stateBudgetFunds;
}
