package com.company.bus_mgmt.web.controller.routes;

import com.company.bus_mgmt.service.schedule.StopService;
import com.company.bus_mgmt.web.dto.stop.StopCreateToRouteRequest;
import com.company.bus_mgmt.web.dto.stop.StopResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stops")
public class StopController {

    private final StopService stops;

    public StopController(StopService stops) { this.stops = stops; }

    // POST: create a stop and attach it to a route by route NAME
    @PostMapping
    public StopResponse createAndAttach(@Valid @RequestBody StopCreateToRouteRequest req) {
        return stops.createAndAttach(req);
    }

    // GET: search by stop name
    @GetMapping
    public List<StopResponse> search(@RequestParam(required = false, name = "q") String q) {
        return stops.search(q);
    }

    @PutMapping("/{id}")
    public StopResponse rename(@PathVariable Long id, @RequestParam("name") String newName) {
        return stops.rename(id, newName);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { stops.delete(id); }

}
