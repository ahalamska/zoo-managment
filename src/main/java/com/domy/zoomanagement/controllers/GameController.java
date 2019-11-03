package com.domy.zoomanagement.controllers;

import com.domy.zoomanagement.managers.BudgetManager;
import com.domy.zoomanagement.models.Budget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GameController {

    private BudgetManager budgetManager;

    @Autowired
    public GameController(BudgetManager budgetManager) {
        this.budgetManager = budgetManager;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/next")
    public Budget startNewRound() {
        return budgetManager.nextRound();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/newGame")
    public Budget startNewGame() {
        return budgetManager.startNewGame();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/budgets")
    public List<Budget> getBudgets(){
        return budgetManager.getStatistics();
    }
}
