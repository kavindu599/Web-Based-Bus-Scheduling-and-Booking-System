package com.busbooking.seatbooking.controller;

import com.busbooking.seatbooking.dto.GroupBookingRequest;
import com.busbooking.seatbooking.dto.GroupBookingResponse;
import com.busbooking.seatbooking.model.Booking;
import com.busbooking.seatbooking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.createBooking(booking));
    }

    // READ
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id,
                                                 @Valid @RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.updateBooking(id, booking));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    // GROUP BOOKING
    @PostMapping("/group")
    public ResponseEntity<GroupBookingResponse> createGroupBooking(@Valid @RequestBody GroupBookingRequest groupBookingRequest) {
        GroupBookingResponse response = bookingService.createGroupBooking(groupBookingRequest);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }


}

