package com.busbooking.seatbooking.controller;

import com.busbooking.seatbooking.dto.GroupBookingRequest;
import com.busbooking.seatbooking.dto.GroupBookingResponse;
import com.busbooking.seatbooking.model.Seat;
import com.busbooking.seatbooking.model.Booking;
import com.busbooking.seatbooking.repository.SeatRepository;
import com.busbooking.seatbooking.service.BookingService;
import com.busbooking.seatbooking.service.PricingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/seatmap")
public class SeatMapController {

    private final SeatRepository seatRepository;
    private final BookingService bookingService;
    private final PricingService pricingService;

    public SeatMapController(SeatRepository seatRepository, BookingService bookingService, PricingService pricingService) {
        this.seatRepository = seatRepository;
        this.bookingService = bookingService;
        this.pricingService = pricingService;
    }

    // Show seat map for a bus
    @GetMapping("/{busNumber}")
    public String showSeatMap(@PathVariable String busNumber, 
                             @RequestParam(value = "success", required = false) String successMessage,
                             @RequestParam(value = "error", required = false) String errorMessage,
                             Model model) {
        List<Seat> seats = seatRepository.findByBusNumber(busNumber);
        
        // If no seats exist for this bus, initialize them (e.g., 40 seats)
        if (seats.isEmpty()) {
            for (int i = 1; i <= 40; i++) {
                Seat seat = new Seat();
                seat.setBusNumber(busNumber);
                seat.setSeatNumber(i);
                seat.setBooked(false);
                seatRepository.save(seat);
                seats.add(seat);
            }
        }
        
        model.addAttribute("busNumber", busNumber);
        model.addAttribute("seats", seats);
        model.addAttribute("booking", new Booking());
        model.addAttribute("groupBooking", new GroupBookingRequest());
        
        // Add city data for origin/destination dropdowns
        model.addAttribute("cities", pricingService.getAllCities());
        
        // Add success/error messages if present
        if (successMessage != null) {
            model.addAttribute("success", successMessage);
        }
        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
        }
        
        return "seatmap";
    }

    // Handle booking form submission
    @PostMapping("/book")
    public String bookSeat(@Valid @ModelAttribute Booking booking, BindingResult result, Model model) {
        // Check for validation errors
        if (result.hasErrors()) {
            List<Seat> seats = seatRepository.findByBusNumber(booking.getBusNumber());
            model.addAttribute("busNumber", booking.getBusNumber());
            model.addAttribute("seats", seats);
            model.addAttribute("groupBooking", new GroupBookingRequest());
            model.addAttribute("error", "Validation failed: " + 
                result.getAllErrors().get(0).getDefaultMessage());
            return "seatmap";
        }
        
        try {
            Booking savedBooking = bookingService.createBooking(booking);
            String successMessage = "Booking confirmed for seat " + savedBooking.getSeatNumber() + 
                    " on bus " + savedBooking.getBusNumber() + 
                    ". Route: " + savedBooking.getOrigin() + " to " + savedBooking.getDestination() +
                    ". Ticket Price: $" + String.format("%.2f", savedBooking.getTicketPrice());
            return "redirect:/seatmap/" + booking.getBusNumber() + "?success=" + successMessage;
        } catch (Exception e) {
            return "redirect:/seatmap/" + booking.getBusNumber() + "?error=" + e.getMessage();
        }
    }

    // Handle group booking form submission
    @PostMapping("/book-group")
    public String bookGroupSeats(@ModelAttribute("groupBooking") GroupBookingRequest groupBookingRequest,
                                @RequestParam(value = "selectedSeats", required = false) String selectedSeats,
                                Model model) {
        try {
            // Parse selected seats from comma-separated string
            if (selectedSeats != null && !selectedSeats.trim().isEmpty()) {
                List<Integer> seatNumbers = Arrays.stream(selectedSeats.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                
                // Validate seat numbers are within range
                for (Integer seatNumber : seatNumbers) {
                    if (seatNumber < 1 || seatNumber > 40) {
                        return "redirect:/seatmap/" + groupBookingRequest.getBusNumber() + "?error=Seat number " + seatNumber + " is invalid. Seat numbers must be between 1 and 40.";
                    }
                }
                
                groupBookingRequest.setSeatNumbers(seatNumbers);
            } else {
                return "redirect:/seatmap/" + groupBookingRequest.getBusNumber() + "?error=Please select at least one seat for group booking.";
            }
            
            // Basic validation
            if (groupBookingRequest.getPassengerName() == null || groupBookingRequest.getPassengerName().trim().isEmpty()) {
                return "redirect:/seatmap/" + groupBookingRequest.getBusNumber() + "?error=Passenger name is required.";
            }
            if (groupBookingRequest.getPhone() == null || !groupBookingRequest.getPhone().matches("^\\d{10}$")) {
                return "redirect:/seatmap/" + groupBookingRequest.getBusNumber() + "?error=Phone number must be exactly 10 digits.";
            }
            if (groupBookingRequest.getEmail() == null || groupBookingRequest.getEmail().trim().isEmpty()) {
                return "redirect:/seatmap/" + groupBookingRequest.getBusNumber() + "?error=Email is required.";
            }
            if (groupBookingRequest.getBusNumber() == null || groupBookingRequest.getBusNumber().trim().isEmpty()) {
                return "redirect:/seatmap/BUS001?error=Bus number is required.";
            }
            if (groupBookingRequest.getOrigin() == null || groupBookingRequest.getOrigin().trim().isEmpty()) {
                return "redirect:/seatmap/" + groupBookingRequest.getBusNumber() + "?error=Origin city is required.";
            }
            if (groupBookingRequest.getDestination() == null || groupBookingRequest.getDestination().trim().isEmpty()) {
                return "redirect:/seatmap/" + groupBookingRequest.getBusNumber() + "?error=Destination city is required.";
            }
            
            GroupBookingResponse response = bookingService.createGroupBooking(groupBookingRequest);
            
            if (response.isSuccess()) {
                return "redirect:/seatmap/" + groupBookingRequest.getBusNumber() + "?success=" + response.getMessage();
            } else {
                return "redirect:/seatmap/" + groupBookingRequest.getBusNumber() + "?error=" + response.getMessage();
            }
            
        } catch (Exception e) {
            return "redirect:/seatmap/" + groupBookingRequest.getBusNumber() + "?error=Error processing group booking: " + e.getMessage();
        }
    }
    
    // REST endpoint to get destinations for a given origin
    @GetMapping("/api/destinations/{origin}")
    @ResponseBody
    public String[] getDestinations(@PathVariable String origin) {
        return pricingService.getDestinationsForOrigin(origin);
    }
    
    // REST endpoint to get ticket price
    @GetMapping("/api/price/{origin}/{destination}/{busNumber}")
    @ResponseBody
    public double getTicketPrice(@PathVariable String origin, 
                                @PathVariable String destination, 
                                @PathVariable String busNumber) {
        return pricingService.calculateTicketPrice(origin, destination, busNumber);
    }
}

