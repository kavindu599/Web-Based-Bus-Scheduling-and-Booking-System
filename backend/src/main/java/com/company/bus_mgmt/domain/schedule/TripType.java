package com.company.bus_mgmt.domain.schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TripType {
    ONE_TIME, SCHEDULED;

    /**
     * Tolerant parser:
     *  - case-insensitive
     *  - accepts spaces and hyphens (ONE TIME, ONE-TIME)
     *  - throws a clear error for invalid values
     */
    @JsonCreator
    public static TripType from(Object raw) {
        if (raw == null) return null;
        String s = raw.toString().trim();
        if (s.isEmpty()) return null;

        String norm = s.toUpperCase()
                .replace('-', '_')
                .replace(' ', '_');

        return switch (norm) {
            case "ONE_TIME" -> ONE_TIME;
            case "SCHEDULED" -> SCHEDULED;
            default -> throw new IllegalArgumentException("Invalid TripType: '" + s + "'. Use ONE_TIME or SCHEDULED.");
        };
    }

    /** Controls how we serialize the enum back to JSON. */
    @JsonValue
    public String toJson() {
        return name();
    }
}
