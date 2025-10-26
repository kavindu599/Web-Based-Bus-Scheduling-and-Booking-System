package com.company.bus_mgmt.service.schedule;

import com.company.bus_mgmt.web.dto.stop.StopCreateToRouteRequest;
import com.company.bus_mgmt.web.dto.stop.StopResponse;

import java.util.List;

public interface StopService {
    StopResponse createAndAttach(StopCreateToRouteRequest req);
    List<StopResponse> search(String q);
    StopResponse rename(Long stopId, String newName);
    void delete(Long stopId);
}
