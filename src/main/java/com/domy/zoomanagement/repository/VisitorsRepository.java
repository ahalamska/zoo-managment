package com.domy.zoomanagement.repository;

import com.domy.zoomanagement.models.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitorsRepository extends JpaRepository<Visitor, Long> {

    List<Visitor> findAll();

    @Query( nativeQuery = true,
    value = "DELETE * FROM visitors WHERE ")
    void deleteOld();

}
