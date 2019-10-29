package com.domy.zoomanagement.repository;

import com.domy.zoomanagement.models.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BudgetsRepository extends JpaRepository<Budget, Date> {

    Budget findCurrent();

}
