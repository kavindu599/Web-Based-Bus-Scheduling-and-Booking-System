package com.company.bus_mgmt.web.dto.trip;

public record TripCompactItem(Long id, String label) {
    public static TripCompactItem fromResponse(TripResponse r) {
        String lbl = "#" + r.id() + " • " + r.routeName() + " • " + r.departureTime();
        return new TripCompactItem(r.id(), lbl);
    }
}
