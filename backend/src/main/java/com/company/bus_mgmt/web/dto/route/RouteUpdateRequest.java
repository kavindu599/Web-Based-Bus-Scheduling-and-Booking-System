package com.company.bus_mgmt.web.dto.route;

import jakarta.validation.constraints.*;

public record RouteUpdateRequest(
        @Size(min=3,max=120) String name,
        @Size(min=2,max=120) String origin,
        @Size(min=2,max=120) String destination,
        @PositiveOrZero Double distanceKm,
        @Min(0) Integer durationMin,
        @Pattern(regexp="ACTIVE|INACTIVE") String status
) {}
