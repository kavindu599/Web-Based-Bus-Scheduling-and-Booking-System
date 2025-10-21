package com.busbooking.seatbooking.dto;

import com.busbooking.seatbooking.model.Booking;
import java.util.List;

public class GroupBookingResponse {
    
    private List<Booking> bookings;
    private String message;
    private boolean success;

    public GroupBookingResponse() {}

    public GroupBookingResponse(List<Booking> bookings, String message, boolean success) {
        this.bookings = bookings;
        this.message = message;
        this.success = success;
    }

    // ---------------- Getters & Setters ----------------
    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

