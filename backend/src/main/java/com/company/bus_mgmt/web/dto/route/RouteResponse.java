package com.company.bus_mgmt.web.dto.route;

import com.company.bus_mgmt.domain.schedule.Route;

import java.time.Instant;

public record RouteResponse(Long id, String name, String origin, String destination,
                            Double distanceKm, Integer durationMin, String status, Instant createdAt) {
    public static RouteResponse from(Route r){
        return new RouteResponse(r.getId(), r.getName(), r.getOrigin(), r.getDestination(),
                r.getDistanceKm(), r.getDurationMin(), r.getStatus(), r.getCreatedAt());
    }
}
