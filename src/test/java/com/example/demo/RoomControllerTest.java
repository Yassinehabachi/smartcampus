package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RoomControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void rootEndpointGreets() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .content().string("Bienvenue sur SmartCampus !"));
    }

    @Test
    void listRoomsReturnsSeededRooms() throws Exception {
        mvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(3)));
    }

    @Test
    void createRoomPersistsAndIsReadable() throws Exception {
        String body = "{\"name\":\"Salle Test\",\"capacity\":12}";
        mvc.perform(post("/api/rooms").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name", is("Salle Test")));
    }

    @Test
    void unknownRoomReturns404() throws Exception {
        mvc.perform(get("/api/rooms/999999")).andExpect(status().isNotFound());
    }

    @Test
    void reservationForUnknownRoomIsRejected() throws Exception {
        String body = "{\"roomId\":999999,\"reservedBy\":\"X\",\"date\":\"2026-12-01\",\"timeSlot\":\"10:00-12:00\"}";
        mvc.perform(post("/api/reservations").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void reservationForSeededRoomIsPersisted() throws Exception {
        String body = "{\"roomId\":1,\"reservedBy\":\"Test\",\"date\":\"2026-12-02\",\"timeSlot\":\"14:00-16:00\"}";
        mvc.perform(post("/api/reservations").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
        mvc.perform(get("/api/reservations")).andExpect(status().isOk());
    }
}
