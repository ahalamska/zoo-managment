package com.domy.zoomanagement.repository;

import com.domy.zoomanagement.models.Enclosure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnclosureRepository extends JpaRepository<Enclosure, Long> {

    List<Enclosure> findAll();

    @Query(nativeQuery = true,
            value = "UPDATE enclosure SET bought = false WHERE bought = true")
    void resetEnclosures();
}
