package com.domy.zoomanagement.controllers;

import com.domy.zoomanagement.managers.GameManager;
import com.domy.zoomanagement.models.Budget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GameController {

    private GameManager gameManager;

    @Autowired
    public GameController(GameManager gameManager) {
        this.gameManager = gameManager;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/next")
    public Budget startNewRound() {
        return gameManager.nextRound();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/newGame")
    public Budget startNewGame() {
        return gameManager.startNewGame();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/budgets")
    public List<Budget> getBudgets(){
        return gameManager.getStatistics();
    }
}
