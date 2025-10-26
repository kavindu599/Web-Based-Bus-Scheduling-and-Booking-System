package com.busbooking.seatbooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Passenger name is required")
    private String passengerName;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
    private String phone;

    @NotNull(message = "Seat number is required")
    @Min(value = 1, message = "Seat number must be at least 1")
    @Max(value = 40, message = "Seat number must be 40 or below")
    private Integer seatNumber;

    @NotBlank(message = "Bus number is required")
    private String busNumber;

    @NotBlank(message = "Origin is required")
    private String origin;

    @NotBlank(message = "Destination is required")
    private String destination;

    private Double ticketPrice;

    private LocalDateTime bookingTime;
    private String status; // CONFIRMED, CANCELLED

    @PrePersist
    public void onCreate() {
        bookingTime = LocalDateTime.now();
        status = "CONFIRMED";
    }

    // ---------------- Getters & Setters ----------------
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getPassengerName() { return passengerName; }

    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public Integer getSeatNumber() { return seatNumber; }

    public void setSeatNumber(Integer seatNumber) { this.seatNumber = seatNumber; }

    public String getBusNumber() { return busNumber; }

    public void setBusNumber(String busNumber) { this.busNumber = busNumber; }

    public LocalDateTime getBookingTime() { return bookingTime; }

    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getOrigin() { return origin; }

    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }

    public void setDestination(String destination) { this.destination = destination; }

    public Double getTicketPrice() { return ticketPrice; }

    public void setTicketPrice(Double ticketPrice) { this.ticketPrice = ticketPrice; }
}
