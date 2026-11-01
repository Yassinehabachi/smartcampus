package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final List<Reservation> reservations = new ArrayList<>(List.of(
        new Reservation(1L, 1L, "Yassine", "2026-11-01", "08:30-10:30")
    ));

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservations;
    }

    @PostMapping
    public Reservation createReservation(@RequestBody Reservation res) {
        res.setId((long) (reservations.size() + 1));
        reservations.add(res);
        return res;
    }
}
