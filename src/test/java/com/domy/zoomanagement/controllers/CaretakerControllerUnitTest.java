package com.domy.zoomanagement.controllers;

import com.domy.zoomanagement.managers.EmployeesManager;
import com.domy.zoomanagement.models.Caretaker;
import com.domy.zoomanagement.models.Contract;
import com.domy.zoomanagement.models.Room;
import com.domy.zoomanagement.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class CaretakerControllerUnitTest {

    private CaretakerRepository caretakersRepository = Mockito.mock(CaretakerRepository.class);

    private ContractsRepository contractsRepository = Mockito.mock(ContractsRepository.class);

    private final RoomRepository roomRepository = Mockito.mock(RoomRepository.class);

    private EmployeesManager caretakerManager = Mockito.mock(EmployeesManager.class);

    private CaretakerController sut;

    private Caretaker caretaker;

    @BeforeEach
    void setUp(){
        sut = new CaretakerController(caretakersRepository, contractsRepository,
                roomRepository, caretakerManager);

        Contract contract = Mockito.mock(Contract.class);

        caretaker = Caretaker.builder()
            .contract(contract)
            .build();
    }

    @Test
    void shouldDeleteCaretakerAndHisContract() {
        when(roomRepository.findAllNotEmptyByCaretaker(caretaker.getId()))
                .thenReturn(Optional.empty());

        when(caretakersRepository.findById(caretaker.getId()))
                .thenReturn(Optional.ofNullable(caretaker));

        assertEquals(ResponseEntity.ok().body("Deleted caretaker and his contract"),
                sut.deleteCaretaker(caretaker.getId()));

        verify(caretakersRepository, times(1)).delete(caretaker);

        verify(contractsRepository, times(1)).delete(caretaker.getContract());

    }

    @Test
    void shouldNotDeleteCaretakerWhenHeHasRoomsToCare() {
        when(roomRepository.findAllNotEmptyByCaretaker(caretaker.getId()))
                .thenReturn(Optional.of(singletonList(new Room())));

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> sut.deleteCaretaker(caretaker.getId()));

        assertEquals("Cannot delete caretaker: there are rooms that need him!", thrown.getMessage());

        verify(caretakersRepository, times(0)).delete(caretaker);
        verify(contractsRepository, times(0)).delete(caretaker.getContract());

    }
}