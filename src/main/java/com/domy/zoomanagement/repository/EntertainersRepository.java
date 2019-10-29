package com.domy.zoomanagement.repository;

import com.domy.zoomanagement.models.Entertainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntertainersRepository extends JpaRepository<Entertainer, Long> {

    List<Entertainer> findAll();
}
