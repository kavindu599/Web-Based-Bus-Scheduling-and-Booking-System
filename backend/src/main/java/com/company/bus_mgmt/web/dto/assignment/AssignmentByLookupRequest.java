package com.company.bus_mgmt.web.dto.assignment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AssignmentByLookupRequest(
        @NotNull Long tripId,
        @NotBlank String busPlate,
        @NotBlank String driverName
) {}
