package com.domy.zoomanagement.controllers;

import com.domy.zoomanagement.models.Room;
import com.domy.zoomanagement.models.Species;
import com.domy.zoomanagement.repository.AnimalsRepository;
import com.domy.zoomanagement.repository.RoomRepository;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RoomControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    RoomRepository repository;

    @MockBean
    AnimalsRepository animalsRepository;

    HttpClient client = new DefaultHttpClient();

    private List<Room> rooms;

    @Before
    public void setUp() {
        Room room = Room.builder()
                .id(1L)
                .locatorsMaxNumber(5)
                .price(12000F)
                .species(asList(Mockito.mock(Species.class)))
                .surface(120F)
                .build();
        rooms = asList(room);
    }

    @Test
    public void shouldGetAllRooms() throws Exception {

        //given((repository.findAll())).willReturn(rooms);

        HttpUriRequest request = new HttpGet("http://localhost:8080/rooms");

        HttpResponse response = client.execute(request);

        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());

    }

    @Test
    public void shouldGetAnimalsInRoom() {
    }

    @Test
    public void shouldCreateRoom() {
    }

    @Test
    public void shouldDeleteRoom() {
    }
}