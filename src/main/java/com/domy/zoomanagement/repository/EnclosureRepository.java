package com.domy.zoomanagement.repository;

import com.domy.zoomanagement.models.Contract;
import com.domy.zoomanagement.models.Enclosure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnclosureRepository extends JpaRepository<Enclosure, Long> {

    List<Enclosure> findAll();
}
