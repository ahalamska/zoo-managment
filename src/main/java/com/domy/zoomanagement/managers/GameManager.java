package com.domy.zoomanagement.managers;

import com.domy.zoomanagement.models.Budget;
import com.domy.zoomanagement.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static java.lang.Math.round;
import static java.util.Arrays.asList;

@Component
@AllArgsConstructor
public class GameManager {

    static final Float BEGINNING_FUNDS = 1000000F;
    static final Float BEGINNING_EU_FUNDS = 100000F;
    static final Float BEGINNING_STATE_FUNDS = 50000F;
    private VisitorManager visitorManager;
    private BudgetsRepository budgetsRepository;
    private ContractsRepository contractsRepository;
    private AnimalsRepository animalsRepository;
    private EntertainersRepository entertainersRepository;
    private EnclosureRepository enclosureRepository;
    private RoomRepository roomRepository;
    private EmployeesManager employeesManager;
    private Budget budget;

    @Autowired
    public GameManager(VisitorManager visitorManager, BudgetsRepository budgetsRepository,
            ContractsRepository contractsRepository, AnimalsRepository animalsRepository,
            EntertainersRepository entertainersRepository, EnclosureRepository enclosureRepository,
            RoomRepository roomRepository, EmployeesManager employeesManager) {
        this.visitorManager = visitorManager;
        this.budgetsRepository = budgetsRepository;
        this.contractsRepository = contractsRepository;
        this.animalsRepository = animalsRepository;
        this.entertainersRepository = entertainersRepository;
        this.enclosureRepository = enclosureRepository;
        this.roomRepository = roomRepository;
        this.employeesManager = employeesManager;
        Budget currentBudget = budgetsRepository.findCurrent();
        if(currentBudget == null){
            startNewGame();
        }
        else {
            this.budget = currentBudget;
        }
    }

    public Budget startNewGame() {
        animalsRepository.deleteAll();
        roomRepository.resetRooms();
        enclosureRepository.resetEnclosures();
        employeesManager.fireEmployees();
        visitorManager.cleanVisitorsRepository();
        budgetsRepository.deleteAll();
        resetBudget();
        return budget;
    }

    public Budget nextRound() {
        budget.setRoundDate(budget.getRoundDate().plusMonths(1));
        countHappinessRate();
        if (budget.getRoundDate().getMonth() == Month.DECEMBER) {
            makeAnnualStatement();
        }
        getMoneyFromVisitors();
        payContractors();
        getMoneyFromFunds();
        budgetsRepository.save(budget);
        return budget;
    }

    public void buy(Float amount) {
        if ((this.budget.getAvailableFunds() - amount) < 0)
            throw new IllegalStateException("Cannot buy : not enough money");
        this.budget.subtractMoney(amount);
    }

    public void sell(Float amount) {
        this.budget.addMoney(amount);
    }

    private void countHappinessRate() {
        Integer animalPoints = animalsRepository.getSumOfPrestigePoints() != null ? animalsRepository.getSumOfPrestigePoints() : 0;
        Float ticketPriceRate = countTicketPriceRate(budget.getTicketPrice());
        long entertainersPoints = entertainersRepository.count() * 5;
        long enclosurePoints = enclosureRepository.count() * 5;
        budget.setHappinessRate(round((animalPoints + entertainersPoints + enclosurePoints) * ticketPriceRate));
    }

    private Float countTicketPriceRate(Float ticketPrice) {
        if (ticketPrice <= 15) return 1f;
        if (ticketPrice > 100) return 0.1f;

        return 1 - ticketPrice / 120;
    }

    private void getMoneyFromFunds() {
        budget.addMoney(budget.getEuFunds() + budget.getStateBudgetFunds());
    }


    private void makeAnnualStatement() {
        budget.setEuFunds(negotiateFundsFromEU());
        budget.setStateBudgetFunds(negotiateFundsFromStateBudget());
        visitorManager.deleteOldStoredVisitors();
    }


    private void getMoneyFromVisitors() {
        int visitorsNumber = generateVisitorNumber();
        budget.addMoney(visitorManager.generateVisitors(visitorsNumber, budget.getTicketPrice()));
    }

    private Integer generateVisitorNumber() {
        float seasonRate = 1f;
        int monthValue = budget.getRoundDate().getMonthValue();
        if (asList(11, 12, 1, 2).contains(monthValue)) seasonRate = 0.5f;
        if (asList(7, 8).contains(monthValue)) seasonRate = 3f;
        return round((new SecureRandom()).nextInt(100) * seasonRate * budget.getHappinessRate());
    }


    private Float negotiateFundsFromEU() {
        if ((new SecureRandom()).nextBoolean()) {
            return (float) (budget.getHappinessRate() * 500);
        }
        return 0f;
    }

    private Float negotiateFundsFromStateBudget() {
        return (float) (budget.getHappinessRate() * 500);
    }

    private void payContractors() {
        Float contractorsPayments = contractsRepository.getContractorsPayment();
        contractorsPayments = contractorsPayments != null ? contractorsPayments : 0;
        budget.subtractMoney(contractorsPayments);
    }

    private void resetBudget() {
        budget = new Budget(LocalDate.now(), 0, 30f,
                BEGINNING_FUNDS, BEGINNING_EU_FUNDS, BEGINNING_STATE_FUNDS);
        budgetsRepository.save(budget);
    }

    public List<Budget> getStatistics() {
        return budgetsRepository.findAll();
    }
}
