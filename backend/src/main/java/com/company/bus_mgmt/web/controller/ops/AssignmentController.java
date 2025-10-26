package com.company.bus_mgmt.web.controller.ops;

import com.company.bus_mgmt.service.ops.AssignmentService;
import com.company.bus_mgmt.web.dto.assignment.AssignmentByLookupRequest;
import com.company.bus_mgmt.web.dto.trip.TripResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService svc;

    public AssignmentController(AssignmentService svc) { this.svc = svc; }

    @PostMapping("/by-lookup")
    public TripResponse assignByLookup(@Valid @RequestBody AssignmentByLookupRequest req) {
        return svc.assignByLookup(req);
    }

    @GetMapping("/available-buses")
    public List<String> availableBuses() { return svc.availableBuses(); }

    @GetMapping("/available-drivers")
    public List<String> availableDrivers() { return svc.availableDrivers(); }
}
