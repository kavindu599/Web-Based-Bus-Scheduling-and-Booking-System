package com.company.bus_mgmt.web.dto.route;

import jakarta.validation.constraints.*;

public record RouteCreateRequest(
        @NotBlank @Size(min=3,max=120) String name,
        @NotBlank @Size(min=2,max=120) String origin,
        @NotBlank @Size(min=2,max=120) String destination,
        @PositiveOrZero Double distanceKm,
        @NotNull @Min(0) Integer durationMin,
        @Pattern(regexp="ACTIVE|INACTIVE") String status
) {}
