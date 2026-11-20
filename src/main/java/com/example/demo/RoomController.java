package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
        // Données d'initialisation si la base est vide
        if (roomRepository.count() == 0) {
            roomRepository.save(new Room("Amphi Ibn Battuta", 250));
            roomRepository.save(new Room("Salle TP DevOps B2", 35));
            roomRepository.save(new Room("Laboratoire IA & Cloud", 20));
        }
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @PostMapping
    public Room createRoom(@RequestBody Room room) {
        return roomRepository.save(
            
        );
    }
}
