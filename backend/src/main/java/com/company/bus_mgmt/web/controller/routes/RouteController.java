package com.company.bus_mgmt.web.controller.routes;

import com.company.bus_mgmt.service.schedule.RouteService;
import com.company.bus_mgmt.web.dto.route.RouteCreateRequest;
import com.company.bus_mgmt.web.dto.route.RouteResponse;
import com.company.bus_mgmt.web.dto.route.RouteUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
public class RouteController {
    private final RouteService routes;

    public RouteController(RouteService routes) {
        this.routes = routes;
    }

    @GetMapping
    public Page<RouteResponse> search(@RequestParam(required=false) String name,
                                      @RequestParam(required=false) String origin,
                                      @RequestParam(required=false) String destination,
                                      @RequestParam(defaultValue="1") int page,
                                      @RequestParam(defaultValue="20") int size,
                                      @RequestParam(required=false) String sort) {
        var pr = PageRequest.of(Math.max(0, page-1), size,
                sort == null ? Sort.unsorted() : Sort.by(sort));
        return routes.search(name, origin, destination, pr);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RouteResponse create(@Valid @RequestBody RouteCreateRequest req) {
        return routes.create(req);
    }

    @GetMapping("/{id}")
    public RouteResponse get(@PathVariable Long id) { return routes.get(id); }

    @PutMapping("/{id}")
    public RouteResponse update(@PathVariable Long id, @Valid @RequestBody RouteUpdateRequest req) {
        return routes.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { routes.delete(id); }
}
