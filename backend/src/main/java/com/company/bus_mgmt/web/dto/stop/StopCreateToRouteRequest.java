package com.company.bus_mgmt.web.dto.stop;

import jakarta.validation.constraints.*;

public record StopCreateToRouteRequest(
        @NotBlank String stopName,
        @NotBlank String routeName,        // assign stop to this route (by name)
        @Min(1) int stopOrder,
        @Min(0) int arrivalOffsetMin       // minutes from route departure to reach this stop
) {}
