package com.busbooking.seatbooking.controller;

import com.busbooking.seatbooking.model.Booking;
import com.busbooking.seatbooking.service.BookingService;
import com.busbooking.seatbooking.service.PricingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
public class ManageBookingsController {

    private final BookingService bookingService;
    private final PricingService pricingService;

    public ManageBookingsController(BookingService bookingService, PricingService pricingService) {
        this.bookingService = bookingService;
        this.pricingService = pricingService;
    }

    @GetMapping("/manage-bookings")
    public String manageBookings(@RequestParam(value = "success", required = false) String successMessage,
                                 @RequestParam(value = "error", required = false) String errorMessage,
                                 Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        if (successMessage != null) model.addAttribute("success", successMessage);
        if (errorMessage != null) model.addAttribute("error", errorMessage);
        return "manage-bookings";
    }

    @PostMapping("/manage-bookings/delete")
    public String deleteBooking(@RequestParam("bookingId") Long bookingId) {
        try {
            bookingService.deleteBooking(bookingId);
            return "redirect:/manage-bookings?success=Booking removed successfully";
        } catch (Exception e) {
            return "redirect:/manage-bookings?error=" + e.getMessage();
        }
    }

    @PostMapping("/manage-bookings/approve")
    public String approveBooking(@RequestParam("bookingId") Long bookingId) {
        try {
            bookingService.approveBooking(bookingId);
            return "redirect:/confirmations/" + bookingId;
        } catch (Exception e) {
            return "redirect:/manage-bookings?error=" + e.getMessage();
        }
    }

    // AJAX-friendly approve endpoint that returns JSON so the UI can update in-place
    @PostMapping(value = "/manage-bookings/approve-ajax", produces = "application/json")
    @ResponseBody
    public java.util.Map<String, Object> approveBookingAjax(@RequestParam("bookingId") Long bookingId) {
        try {
            bookingService.approveBooking(bookingId);
            return java.util.Map.of("success", true, "bookingId", bookingId);
        } catch (Exception e) {
            // unwrap root cause
            Throwable t = e;
            while (t.getCause() != null) t = t.getCause();
            return java.util.Map.of("success", false, "error", t.getMessage() != null ? t.getMessage() : e.getMessage());
        }
    }

    @GetMapping("/edit-booking/{id}")
    public String editBookingForm(@PathVariable Long id, Model model) {
        Booking booking = bookingService.getBookingById(id);
        model.addAttribute("booking", booking);
        model.addAttribute("cities", pricingService.getAllCities());
        return "edit-booking";
    }

    @PostMapping("/edit-booking/{id}")
    public String editBookingSubmit(@PathVariable Long id,
                                    @Valid @ModelAttribute Booking booking,
                                    BindingResult result,
                                    Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cities", pricingService.getAllCities());
            return "edit-booking";
        }
        try {
            bookingService.updateBooking(id, booking);
            return "redirect:/manage-bookings?success=Booking updated successfully";
        } catch (Exception e) {
            return "redirect:/manage-bookings?error=" + e.getMessage();
        }
    }
}
