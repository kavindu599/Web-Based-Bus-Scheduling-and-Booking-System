package com.busbooking.seatbooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Seat number is required")
    @Min(value = 1, message = "Seat number must be at least 1")
    @Max(value = 40, message = "Seat number must be 40 or below")
    private Integer seatNumber;
    
    @NotNull(message = "Bus number is required")
    private String busNumber;
    private boolean booked;

    // ---------------- Getters & Setters ----------------
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Integer getSeatNumber() { return seatNumber; }

    public void setSeatNumber(Integer seatNumber) { this.seatNumber = seatNumber; }

    public String getBusNumber() { return busNumber; }

    public void setBusNumber(String busNumber) { this.busNumber = busNumber; }

    public boolean isBooked() { return booked; }

    public void setBooked(boolean booked) { this.booked = booked; }
}
