package com.domy.zoomanagement.repository;

import com.domy.zoomanagement.models.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractsRepository extends JpaRepository<Contract, Long> {

    List<Contract> findAll();

    @Query(nativeQuery = true,
    value = "SELECT SUM(payment) FROM contract")
    Float getContractorsPayment();
}
