package com.busbooking.seatbooking.service;

import com.busbooking.seatbooking.dto.GroupBookingRequest;
import com.busbooking.seatbooking.dto.GroupBookingResponse;
import com.busbooking.seatbooking.exception.BookingNotFoundException;
import com.busbooking.seatbooking.model.Booking;
import com.busbooking.seatbooking.model.Seat;
import com.busbooking.seatbooking.repository.BookingRepository;
import com.busbooking.seatbooking.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final PricingService pricingService;

    public BookingService(BookingRepository bookingRepository, SeatRepository seatRepository, PricingService pricingService) {
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
        this.pricingService = pricingService;
    }

    // CREATE booking
    @Transactional
    public Booking createBooking(Booking booking) {
        // Validate seat number range
        if (booking.getSeatNumber() < 1 || booking.getSeatNumber() > 40) {
            throw new IllegalArgumentException("Seat number must be between 1 and 40");
        }
        
        // Validate phone number format
        if (booking.getPhone() == null || !booking.getPhone().matches("^\\d{10}$")) {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits");
        }
        
        // Validate route
        if (!pricingService.isValidRoute(booking.getOrigin(), booking.getDestination())) {
            throw new IllegalArgumentException("Invalid route from " + booking.getOrigin() + " to " + booking.getDestination());
        }
        
        // Calculate and set ticket price
        double ticketPrice = pricingService.calculateTicketPrice(
                booking.getOrigin(), 
                booking.getDestination(), 
                booking.getBusNumber()
        );
        booking.setTicketPrice(ticketPrice);
        
        Seat seat = seatRepository.findByBusNumberAndSeatNumber(
                booking.getBusNumber(), booking.getSeatNumber()
        );

        if (seat != null && seat.isBooked()) {
            throw new IllegalArgumentException("Seat already booked!");
        }

        if (seat == null) {
            seat = new Seat();
            seat.setBusNumber(booking.getBusNumber());
            seat.setSeatNumber(booking.getSeatNumber());
        }

        seat.setBooked(true);
        seatRepository.save(seat);

        return bookingRepository.save(booking);
    }

    // READ all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // UPDATE booking (change seat)
    @Transactional
    public Booking updateBooking(Long id, Booking updatedBooking) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        // free old seat
        Seat oldSeat = seatRepository.findByBusNumberAndSeatNumber(
                booking.getBusNumber(), booking.getSeatNumber());
        if (oldSeat != null) {
            oldSeat.setBooked(false);
            seatRepository.save(oldSeat);
        }

        // assign new seat
        Seat newSeat = seatRepository.findByBusNumberAndSeatNumber(
                updatedBooking.getBusNumber(), updatedBooking.getSeatNumber());
        if (newSeat != null && newSeat.isBooked()) {
            throw new IllegalArgumentException("New seat is already booked!");
        }
        if (newSeat == null) {
            newSeat = new Seat();
            newSeat.setBusNumber(updatedBooking.getBusNumber());
            newSeat.setSeatNumber(updatedBooking.getSeatNumber());
        }

        newSeat.setBooked(true);
        seatRepository.save(newSeat);

        booking.setPassengerName(updatedBooking.getPassengerName());
        booking.setEmail(updatedBooking.getEmail());
        booking.setPhone(updatedBooking.getPhone());
        booking.setBusNumber(updatedBooking.getBusNumber());
        booking.setSeatNumber(updatedBooking.getSeatNumber());

        return bookingRepository.save(booking);
    }

    // DELETE booking
    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        Seat seat = seatRepository.findByBusNumberAndSeatNumber(
                booking.getBusNumber(), booking.getSeatNumber());
        if (seat != null) {
            seat.setBooked(false);
            seatRepository.save(seat);
        }

        bookingRepository.delete(booking);
    }

    // Get single booking by id
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));
    }

    // Approve booking (set status to APPROVED)
    @Transactional
    public void approveBooking(Long id) {
        // Use a direct update query to avoid entity merge/validation issues at commit
        int updated = 0;
        try {
            updated = bookingRepository.updateStatusById(id, "APPROVED");
        } catch (Exception ex) {
            throw new RuntimeException("Failed to approve booking (DB update error): " + ex.getMessage(), ex);
        }
        if (updated == 0) {
            throw new BookingNotFoundException("Booking not found");
        }
    }

    // Return bookings by status
    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status);
    }

    // GROUP BOOKING - Create multiple bookings at once
    @Transactional
    public GroupBookingResponse createGroupBooking(GroupBookingRequest groupBookingRequest) {
        List<Booking> successfulBookings = new ArrayList<>();
        List<Integer> failedSeats = new ArrayList<>();
        
        // Validate phone number format
        if (groupBookingRequest.getPhone() == null || !groupBookingRequest.getPhone().matches("^\\d{10}$")) {
            return new GroupBookingResponse(
                    new ArrayList<>(),
                    "Phone number must be exactly 10 digits",
                    false
            );
        }
        
        // Validate route
        if (!pricingService.isValidRoute(groupBookingRequest.getOrigin(), groupBookingRequest.getDestination())) {
            return new GroupBookingResponse(
                    new ArrayList<>(),
                    "Invalid route from " + groupBookingRequest.getOrigin() + " to " + groupBookingRequest.getDestination(),
                    false
            );
        }
        
        // Validate all seat numbers are within range
        for (Integer seatNumber : groupBookingRequest.getSeatNumbers()) {
            if (seatNumber < 1 || seatNumber > 40) {
                return new GroupBookingResponse(
                        new ArrayList<>(),
                        "Seat number " + seatNumber + " is invalid. Seat numbers must be between 1 and 40.",
                        false
                );
            }
        }
        
        // First, check if all seats are available
        for (Integer seatNumber : groupBookingRequest.getSeatNumbers()) {
            Seat seat = seatRepository.findByBusNumberAndSeatNumber(
                    groupBookingRequest.getBusNumber(), seatNumber);
            if (seat != null && seat.isBooked()) {
                failedSeats.add(seatNumber);
            }
        }
        
        // If any seat is already booked, return error
        if (!failedSeats.isEmpty()) {
            return new GroupBookingResponse(
                    new ArrayList<>(),
                    "The following seats are already booked: " + failedSeats,
                    false
            );
        }
        
        // Calculate ticket price for this route
        double ticketPrice = pricingService.calculateTicketPrice(
                groupBookingRequest.getOrigin(), 
                groupBookingRequest.getDestination(), 
                groupBookingRequest.getBusNumber()
        );
        
        // All seats are available, proceed with booking
        for (Integer seatNumber : groupBookingRequest.getSeatNumbers()) {
            try {
                // Create booking for each seat
                Booking booking = new Booking();
                booking.setPassengerName(groupBookingRequest.getPassengerName());
                booking.setEmail(groupBookingRequest.getEmail());
                booking.setPhone(groupBookingRequest.getPhone());
                booking.setBusNumber(groupBookingRequest.getBusNumber());
                booking.setSeatNumber(seatNumber);
                booking.setOrigin(groupBookingRequest.getOrigin());
                booking.setDestination(groupBookingRequest.getDestination());
                booking.setTicketPrice(ticketPrice);
                
                // Find or create seat
                Seat seat = seatRepository.findByBusNumberAndSeatNumber(
                        groupBookingRequest.getBusNumber(), seatNumber);
                if (seat == null) {
                    seat = new Seat();
                    seat.setBusNumber(groupBookingRequest.getBusNumber());
                    seat.setSeatNumber(seatNumber);
                }
                
                seat.setBooked(true);
                seatRepository.save(seat);
                
                Booking savedBooking = bookingRepository.save(booking);
                successfulBookings.add(savedBooking);
                
            } catch (Exception e) {
                // If any booking fails, we should rollback (transaction will handle this)
                throw new RuntimeException("Failed to book seat " + seatNumber + ": " + e.getMessage());
            }
        }
        
        double totalAmount = successfulBookings.size() * ticketPrice;
        String message = "Successfully booked " + successfulBookings.size() + " seats: " + 
                        groupBookingRequest.getSeatNumbers() + " for " + groupBookingRequest.getPassengerName() +
                        ". Route: " + groupBookingRequest.getOrigin() + " to " + groupBookingRequest.getDestination() +
                        ". Total Amount: $" + String.format("%.2f", totalAmount) +
                        " ($" + String.format("%.2f", ticketPrice) + " per ticket)";
        
        return new GroupBookingResponse(successfulBookings, message, true);
    }
}

