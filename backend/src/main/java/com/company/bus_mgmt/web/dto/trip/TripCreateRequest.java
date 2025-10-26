package com.company.bus_mgmt.web.dto.trip;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record TripCreateRequest(
        @NotBlank String routeName,                 // friendly: route by name
        @NotNull @Pattern(regexp="ONE_TIME|SCHEDULED") String tripType,
        LocalDateTime publishAt,                    // required if ONE_TIME
        @NotNull LocalDateTime departureTime,
        @NotNull LocalDateTime arrivalTime
) {}
