package com.busbooking.seatbooking.controller;

import com.busbooking.seatbooking.model.Booking;
import com.busbooking.seatbooking.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/confirmations")
public class ConfirmationsController {

    private final BookingService bookingService;

    public ConfirmationsController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public String listConfirmed(Model model) {
        List<Booking> confirmed = bookingService.getBookingsByStatus("APPROVED");
        model.addAttribute("confirmations", confirmed);
        return "confirmations";
    }

    @GetMapping("/{id}")
    public String confirmationDetail(@PathVariable Long id, Model model) {
        Booking booking = bookingService.getBookingById(id);
        model.addAttribute("booking", booking);
        return "confirmation-detail";
    }
}
