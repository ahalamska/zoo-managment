package com.domy.zoomanagement.managers;

import com.domy.zoomanagement.models.Visitor;
import com.domy.zoomanagement.repository.VisitorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.round;

@Component
public class VisitorManager {

    private List<Visitor> visitors = new ArrayList<>();

    private VisitorsRepository visitorsRepository;


    @Autowired
    public VisitorManager(VisitorsRepository visitorsRepository) {
        this.visitorsRepository = visitorsRepository;
    }


    private void saveVisitors() {
        visitorsRepository.saveAll(visitors);
        visitors.clear();
    }

    Float generateVisitors(int visitorsNumber, float ticketPrice) {
        IntStream.range(0, visitorsNumber).forEach(i -> visitors.add(generateVisitor()));
        return sellTickets(ticketPrice);
    }

    private Visitor generateVisitor() {
        VisitorType visitorType = VisitorType.getRandomType();
        return Visitor.builder().visitorType(visitorType.toString()).build();
    }

    private Float sellTickets(Float ticketPrice) {
        float sum = 0f;
        for(Visitor visitor : visitors){
            float discount = VisitorType.valueOf(visitor.getVisitorType()).getDiscount();
            sum += ((float) round(discount * ticketPrice *100))/100;
        }
        saveVisitors();
        return sum;
    }

    public void deleteOldStoredVisitors() {
        visitorsRepository.deleteAll();
    }
}

