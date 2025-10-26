package com.company.bus_mgmt.web.dto.stop;

import com.company.bus_mgmt.domain.schedule.Stop;

public record StopResponse(Long id, String name) {
    public static StopResponse from(Stop s) { return new StopResponse(s.getId(), s.getName()); }
}
