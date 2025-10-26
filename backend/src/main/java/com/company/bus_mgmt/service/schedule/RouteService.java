package com.company.bus_mgmt.service.schedule;

import com.company.bus_mgmt.web.dto.route.RouteCreateRequest;
import com.company.bus_mgmt.web.dto.route.RouteResponse;
import com.company.bus_mgmt.web.dto.route.RouteUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RouteService {
    Page<RouteResponse> search(String name, String origin, String destination, Pageable pageable);
    RouteResponse create(RouteCreateRequest req);
    RouteResponse get(Long id);
    RouteResponse update(Long id, RouteUpdateRequest req);
    void delete(Long id);
}
