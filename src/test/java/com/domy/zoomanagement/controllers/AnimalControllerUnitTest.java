package com.domy.zoomanagement.controllers;

import com.domy.zoomanagement.managers.GameManager;
import com.domy.zoomanagement.models.Animal;
import com.domy.zoomanagement.models.Caretaker;
import com.domy.zoomanagement.models.Room;
import com.domy.zoomanagement.models.Species;
import com.domy.zoomanagement.repository.AnimalsRepository;
import com.domy.zoomanagement.repository.RoomRepository;
import com.domy.zoomanagement.repository.SpeciesRepository;
import com.domy.zoomanagement.requests.AnimalRequest;
import com.domy.zoomanagement.utils.OptionalUtil;

import static com.domy.zoomanagement.controllers.RoomController.ROOM_NOT_FOUND;
import static com.domy.zoomanagement.controllers.SpeciesController.SPECIES_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AnimalControllerUnitTest {

    private final AnimalsRepository animalsRepository = Mockito.mock(AnimalsRepository.class);
    private final SpeciesRepository speciesRepository = Mockito.mock(SpeciesRepository.class);
    private final RoomRepository roomRepository = Mockito.mock(RoomRepository.class);
    private AnimalController sut;
    private GameManager gameManager = Mockito.mock(GameManager.class);
    private AnimalRequest animalRequest;
    private Species species;
    private Room room;
    private Animal animal;
    private Caretaker caretaker = Mockito.mock(Caretaker.class);

    @BeforeEach
    void setUp() {
        sut = new AnimalController(animalsRepository, speciesRepository, roomRepository, gameManager);
        animalRequest = AnimalRequest.builder()
                .name("A")
                .room(1L)
                .species("S")
                .build();

        species = Species.builder()
                        .name("s")
                        .price(1f)
                        .build();

        room = Room.builder()
                .id(1L)
                .bought(true)
                .caretaker(caretaker)
                .locatorsMaxNumber(2)
                .build();

        animal = Animal.builder().name(OptionalUtil.handleNullable(animalRequest.getName()))
                .species(species)
                .room(room)
                .build();

    }

    @Test
    void shouldBuyAnimal() {
        when(speciesRepository.findByName(animalRequest.getSpecies())).thenReturn(Optional.ofNullable(species));
        when(roomRepository.findById(animalRequest.getRoom())).thenReturn(Optional.ofNullable(room));
        when(roomRepository.getNumberOfOccurrencesPlaces(room.getId())).thenReturn(room.getLocatorsMaxNumber() - 1);
        assertEquals(animal, sut.buyAnimal(animalRequest));

        verify(animalsRepository, times(1)).save(animal);
        verify(gameManager, times(1)).buy(species.getPrice());
    }

    @Test
    void shouldNotBuyAnimalWhenSpeciesNotExist() {

        when(speciesRepository.findByName(animalRequest.getSpecies())).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            sut.buyAnimal(animalRequest);
        });
        assertEquals(SPECIES_NOT_FOUND, thrown.getMessage());
    }

    @Test
    void shouldNotBuyAnimalWhenRoomNotExist() {

        when(speciesRepository.findByName(animalRequest.getSpecies())).thenReturn(Optional.ofNullable(species));
        when(roomRepository.findById(animalRequest.getRoom())).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            sut.buyAnimal(animalRequest);
        });
        assertEquals(ROOM_NOT_FOUND, thrown.getMessage());
    }
    @Test
    void shouldNotBuyAnimalWhenRoomIsNotBought() {

        room.setBought(false);

        when(speciesRepository.findByName(animalRequest.getSpecies())).thenReturn(Optional.ofNullable(species));
        when(roomRepository.findById(animalRequest.getRoom())).thenReturn(Optional.ofNullable(room));

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            sut.buyAnimal(animalRequest);
        });
        assertEquals("Given room is not bought!", thrown.getMessage());
    }

    @Test
    void shouldNotBuyAnimalWhenCaretakerIsNull() {

        room.setCaretaker(null);

        when(speciesRepository.findByName(animalRequest.getSpecies())).thenReturn(Optional.ofNullable(species));
        when(roomRepository.findById(animalRequest.getRoom())).thenReturn(Optional.ofNullable(room));

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            sut.buyAnimal(animalRequest);
        });
        assertEquals("Given room has no caretaker!", thrown.getMessage());
    }

    @Test
    void shouldNotBuyAnimalWhenRoomIsFull() {

        when(speciesRepository.findByName(animalRequest.getSpecies()))
                .thenReturn(Optional.ofNullable(species));

        when(roomRepository.findById(animalRequest.getRoom()))
                .thenReturn(Optional.ofNullable(room));

        when(roomRepository.getNumberOfOccurrencesPlaces(room.getId()))
                .thenReturn(room.getLocatorsMaxNumber());

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            sut.buyAnimal(animalRequest);
        });
        assertEquals("Given room is full!", thrown.getMessage());
    }

    @Test
    void ShouldSellAnimal() {
        when(animalsRepository.findById(animal.getId()))
                .thenReturn(Optional.ofNullable(animal));

        assertEquals(ResponseEntity.ok().build(), sut.sellAnimal(animal.getId()));

        verify(animalsRepository, times(1)).delete(animal);

        verify(gameManager, times(1)).sell(animal.getSpecies().getPrice() / 2);

    }
}