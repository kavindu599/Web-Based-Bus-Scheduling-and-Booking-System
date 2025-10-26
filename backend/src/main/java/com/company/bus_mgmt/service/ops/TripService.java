package com.company.bus_mgmt.service.ops;

import com.company.bus_mgmt.web.dto.trip.TripCompactItem;
import com.company.bus_mgmt.web.dto.trip.TripCreateRequest;
import com.company.bus_mgmt.web.dto.trip.TripResponse;
import com.company.bus_mgmt.web.dto.trip.TripUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TripService {
    TripResponse create(TripCreateRequest req);
    TripResponse get(Long id);
    Page<TripResponse> search(String routeName, LocalDateTime from, LocalDateTime to, Pageable pg);
    List<TripCompactItem> compact(String routeName, LocalDateTime from, LocalDateTime to, int limit);
    void deactivate(Long id);     // for SCHEDULED, sets active=false; for ONE_TIME, sets status=INACTIVE
    void activate(Long id);       // sets active=true (scheduled only)
    void completeIfPast(Long id);

    TripResponse update(Long id, TripUpdateRequest req);
    void delete(Long id); // soft: status=CANCELLED

}
