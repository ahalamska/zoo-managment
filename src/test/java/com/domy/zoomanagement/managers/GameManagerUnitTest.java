package com.domy.zoomanagement.managers;

import com.domy.zoomanagement.models.Budget;
import com.domy.zoomanagement.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static com.domy.zoomanagement.managers.GameManager.BEGINNING_EU_FUNDS;
import static com.domy.zoomanagement.managers.GameManager.BEGINNING_FUNDS;
import static com.domy.zoomanagement.managers.GameManager.BEGINNING_STATE_FUNDS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
class GameManagerUnitTest {

    @Mock
    private VisitorManager visitorManager;
    @Mock
    private BudgetsRepository budgetsRepository;
    @Mock
    private Budget budget;
    @Mock
    private ContractsRepository contractsRepository;
    @Mock
    private AnimalsRepository animalsRepository;
    @Mock
    private EntertainersRepository entertainersRepository;
    @Mock
    private EnclosureRepository enclosureRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private EmployeesManager employeesManager;

    private GameManager sut;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        sut = new GameManager(visitorManager, budgetsRepository, contractsRepository, animalsRepository,
                entertainersRepository, enclosureRepository, roomRepository, employeesManager);
    }

    @Test
    void startNewGameShouldResetEverything() {

        Budget beginningBudget = new Budget(LocalDate.now(), 0, 30f,
                BEGINNING_FUNDS, BEGINNING_EU_FUNDS, BEGINNING_STATE_FUNDS);

        assertEquals(beginningBudget ,sut.startNewGame());
        verify(animalsRepository, times(1)).deleteAll();
        verify(roomRepository, times(1)).resetRooms();
        verify(enclosureRepository, times(1)).resetEnclosures();
        verify(employeesManager, times(1)).fireEmployees();
        verify(visitorManager, times(1)).cleanVisitorsRepository();
        verify(budgetsRepository, times(1)).save(beginningBudget);

    }
}