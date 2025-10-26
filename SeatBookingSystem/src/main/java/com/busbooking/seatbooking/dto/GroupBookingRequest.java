package com.busbooking.seatbooking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.util.List;

public class GroupBookingRequest {

    @NotBlank(message = "Passenger name is required")
    private String passengerName;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
    private String phone;

    @NotBlank(message = "Bus number is required")
    private String busNumber;

    @NotBlank(message = "Origin is required")
    private String origin;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotEmpty(message = "At least one seat must be selected")
    private List<@NotNull @Min(value = 1, message = "Seat number must be at least 1") @Max(value = 40, message = "Seat number must be 40 or below") Integer> seatNumbers;

    // ---------------- Getters & Setters ----------------
    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public List<Integer> getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(List<Integer> seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}

