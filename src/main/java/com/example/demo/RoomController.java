package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final List<Room> rooms = new ArrayList<>(List.of(
        new Room(1L, "Amphi A", 150),
        new Room(2L, "Salle Informatique B1", 30),
        new Room(3L, "Labo Réseau C2", 25)
    ));

    @GetMapping
    public List<Room> getAllRooms() {
        return rooms;
    }

    @PostMapping
    public Room addRoom(@RequestBody Room newRoom) {
        newRoom.setId((long) (rooms.size() + 1));
        rooms.add(newRoom);
        return newRoom;
    }
}
