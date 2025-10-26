package com.company.bus_mgmt.web.dto.trip;

import com.company.bus_mgmt.domain.schedule.Trip;
import com.company.bus_mgmt.domain.schedule.TripType;

import java.time.LocalDateTime;

public record TripResponse(
        Long id,
        String routeName,
        String origin,
        String destination,
        String status,
        String tripType,
        LocalDateTime publishAt,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        Long busId, String busPlate,
        Long driverId, String driverName,
        boolean active
) {
    public static TripResponse from(Trip t) {
        var bus = t.getBus();
        var drv = t.getDriver();
        return new TripResponse(
                t.getId(),
                t.getRoute().getName(),
                t.getRoute().getOrigin(),
                t.getRoute().getDestination(),
                t.getStatus(),
                t.getTripType().name(),
                t.getPublishAt(),
                t.getDepartureTime(),
                t.getArrivalTime(),
                bus == null ? null : bus.getId(),
                bus == null ? null : bus.getPlateNumber(),
                drv == null ? null : drv.getId(),
                drv == null ? null : (drv.getFirstName() + " " + drv.getLastName()),
                t.isActive()
        );
    }
}
