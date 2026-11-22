package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationRepository reservations;
    private final RoomRepository rooms;

    public ReservationController(ReservationRepository reservations, RoomRepository rooms) {
        this.reservations = reservations;
        this.rooms = rooms;
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservations.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation createReservation(@RequestBody Reservation res) {
        if (res.getRoomId() == null || !rooms.existsById(res.getRoomId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown roomId");
        }
        res.setId(null);
        return reservations.save(res);
    }
}
