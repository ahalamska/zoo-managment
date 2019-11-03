package com.domy.zoomanagement.repository;

import com.domy.zoomanagement.models.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BudgetsRepository extends JpaRepository<Budget, Date> {


    @Query(nativeQuery = true,
    value = "SELECT * FROM budget ORDER BY budget.round_date LIMIT 1")
    Budget findCurrent();
}
