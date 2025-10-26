package com.company.bus_mgmt.web.controller.ops;

import com.company.bus_mgmt.service.ops.TripService;
import com.company.bus_mgmt.web.dto.trip.TripCompactItem;
import com.company.bus_mgmt.web.dto.trip.TripCreateRequest;
import com.company.bus_mgmt.web.dto.trip.TripResponse;
import com.company.bus_mgmt.web.dto.trip.TripUpdateRequest;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService trips;
    public TripController(TripService trips) { this.trips = trips; }

    @PostMapping
    public TripResponse create(@Valid @RequestBody TripCreateRequest req) {
        return trips.create(req);
    }

    @GetMapping
    public org.springframework.data.domain.Page<TripResponse> search(
            @RequestParam(required = false) String routeName,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return trips.search(routeName, from, to, PageRequest.of(Math.max(0, page - 1), size));
    }

    @GetMapping("/api/trips/compact")
    public List<TripCompactItem> compact(
            @Parameter(schema = @Schema(type = "string", format = "date-time"),
                    example = "2025-10-20T17:01:31")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @Parameter(schema = @Schema(type = "string", format = "date-time"),
                    example = "2025-10-20T19:01:31")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to,

            @RequestParam(required = false) String routeName,
            @RequestParam(defaultValue = "200") int limit
    ) {
        return trips.compact(routeName, from, to, limit);
    }

    @PostMapping("/{id}:deactivate")
    public void deactivate(@PathVariable Long id) { trips.deactivate(id); }

    @PostMapping("/{id}:activate")
    public void activate(@PathVariable Long id) { trips.activate(id); }

    @PostMapping("/{id}:complete-if-past")
    public void completeIfPast(@PathVariable Long id) { trips.completeIfPast(id); }

    @GetMapping("/{id}")
    public TripResponse get(@PathVariable Long id) { return trips.get(id); }

    @PutMapping("/{id}")
    public TripResponse update(@PathVariable Long id, @RequestBody TripUpdateRequest req) {
        return trips.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { trips.delete(id); }

}
