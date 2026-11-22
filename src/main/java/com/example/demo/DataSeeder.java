package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Inserts demo data (fictional) when the database is empty. */
@Component
public class DataSeeder implements CommandLineRunner {

    private final RoomRepository rooms;
    private final ReservationRepository reservations;

    public DataSeeder(RoomRepository rooms, ReservationRepository reservations) {
        this.rooms = rooms;
        this.reservations = reservations;
    }

    @Override
    public void run(String... args) {
        if (rooms.count() == 0) {
            Room amphi = rooms.save(new Room("Amphi Ibn Battuta", 250));
            rooms.save(new Room("Salle TP DevOps B2", 35));
            rooms.save(new Room("Laboratoire IA & Cloud", 20));
            if (reservations.count() == 0) {
                reservations.save(new Reservation(null, amphi.getId(), "Yassine", "2026-11-01", "08:30-10:30"));
            }
        }
    }
}
