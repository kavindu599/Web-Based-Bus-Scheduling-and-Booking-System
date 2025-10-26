package com.company.bus_mgmt.service_impl.schedule;

import com.company.bus_mgmt.domain.schedule.Route;
import com.company.bus_mgmt.exception.ConflictException;
import com.company.bus_mgmt.exception.NotFoundException;
import com.company.bus_mgmt.repository.schedule.RouteRepository;
import com.company.bus_mgmt.service.schedule.RouteService;
import com.company.bus_mgmt.web.dto.route.RouteCreateRequest;
import com.company.bus_mgmt.web.dto.route.RouteResponse;
import com.company.bus_mgmt.web.dto.route.RouteUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routes;

    public RouteServiceImpl(RouteRepository routes) {
        this.routes = routes;
    }

    @Override
    public Page<RouteResponse> search(String name, String origin, String destination, Pageable pageable) {
        if ((name == null || name.isBlank()) && (origin == null || origin.isBlank()) && (destination == null || destination.isBlank())) {
            return routes.findAll(pageable).map(RouteResponse::from);
        }
        return routes
                .findByNameContainingIgnoreCaseOrOriginContainingIgnoreCaseOrDestinationContainingIgnoreCase(
                        name == null ? "" : name, origin == null ? "" : origin, destination == null ? "" : destination, pageable)
                .map(RouteResponse::from);
    }

    @Override
    public RouteResponse create(RouteCreateRequest req) {
        Route r = new Route();
        r.setName(req.name());
        r.setOrigin(req.origin());
        r.setDestination(req.destination());
        r.setDistanceKm(req.distanceKm() == null ? 0.0 : req.distanceKm());
        r.setDurationMin(req.durationMin());
        r.setStatus(req.status() == null ? "ACTIVE" : req.status());
        try {
            routes.saveAndFlush(r);
        } catch (Exception e) {
            throw new ConflictException("Route name must be unique");
        }
        return RouteResponse.from(r);
    }

    @Override
    public RouteResponse get(Long id) {
        Route r = routes.findById(id).orElseThrow(() -> new NotFoundException("Route not found"));
        return RouteResponse.from(r);
    }

    @Override
    public RouteResponse update(Long id, RouteUpdateRequest req) {
        Route r = routes.findById(id).orElseThrow(() -> new NotFoundException("Route not found"));
        if (req.name() != null) r.setName(req.name());
        if (req.origin() != null) r.setOrigin(req.origin());
        if (req.destination() != null) r.setDestination(req.destination());
        if (req.distanceKm() != null) r.setDistanceKm(req.distanceKm());
        if (req.durationMin() != null) r.setDurationMin(req.durationMin());
        if (req.status() != null) r.setStatus(req.status());
        try {
            return RouteResponse.from(routes.saveAndFlush(r));
        } catch (Exception e) {
            throw new ConflictException("Route name must be unique");
        }
    }

    @Override
    public void delete(Long id) {
        if (!routes.existsById(id)) throw new NotFoundException("Route not found");
        routes.deleteById(id); // DB will reject if trips exist; we catch at controller if needed
    }
}
