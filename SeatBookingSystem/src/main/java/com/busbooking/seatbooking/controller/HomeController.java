package com.busbooking.seatbooking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        return "redirect:/seatmap/BUS001";
    }
    
    @GetMapping("/select-bus")
    public String selectBus() {
        return "select-bus";
    }
    
    @PostMapping("/select-bus")
    public String selectBusPost(@RequestParam(value = "busNumber", required = false) String busNumber,
                               @RequestParam(value = "customBusNumber", required = false) String customBusNumber) {
        String selectedBus = null;
        
        // Check if a bus was selected from dropdown
        if (busNumber != null && !busNumber.trim().isEmpty()) {
            selectedBus = busNumber.trim().toUpperCase();
        }
        // If no dropdown selection, check custom input
        else if (customBusNumber != null && !customBusNumber.trim().isEmpty()) {
            selectedBus = customBusNumber.trim().toUpperCase();
        }
        
        // Validate that at least one bus number is provided
        if (selectedBus == null) {
            return "redirect:/select-bus?error=Please select a bus from the dropdown or enter a custom bus number";
        }
        
        return "redirect:/seatmap/" + selectedBus;
    }
}
