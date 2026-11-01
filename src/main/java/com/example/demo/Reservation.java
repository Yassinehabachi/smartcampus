package com.example.demo;

public class Reservation {
    private Long id;
    private Long roomId;
    private String reservedBy;
    private String date;
    private String timeSlot;

    public Reservation() {}

    public Reservation(Long id, Long roomId, String reservedBy, String date, String timeSlot) {
        this.id = id;
        this.roomId = roomId;
        this.reservedBy = reservedBy;
        this.date = date;
        this.timeSlot = timeSlot;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public String getReservedBy() { return reservedBy; }
    public void setReservedBy(String reservedBy) { this.reservedBy = reservedBy; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
}
