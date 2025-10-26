package com.company.bus_mgmt.service.ops;

import com.company.bus_mgmt.web.dto.assignment.AssignmentByLookupRequest;
import com.company.bus_mgmt.web.dto.trip.TripResponse;

import java.util.List;

public interface AssignmentService {
    TripResponse assignByLookup(AssignmentByLookupRequest req);
    List<String> availableBuses();    // "PLATE (capacity)"
    List<String> availableDrivers();  // "First Last (id: X)"
}
