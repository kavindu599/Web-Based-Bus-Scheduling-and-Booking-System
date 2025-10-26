package com.company.bus_mgmt.web.dto.trip;

import java.time.LocalDateTime;

public record TripUpdateRequest(
        String routeName,               // optional: change route by name
        String tripType,                // optional: ONE_TIME | SCHEDULED
        LocalDateTime publishAt,        // optional: used if tripType ONE_TIME
        LocalDateTime departureTime,    // optional
        LocalDateTime arrivalTime,      // optional
        Boolean active,                 // optional: only for SCHEDULED
        String status                   // optional: SCHEDULED, CANCELLED, etc.
) {}
