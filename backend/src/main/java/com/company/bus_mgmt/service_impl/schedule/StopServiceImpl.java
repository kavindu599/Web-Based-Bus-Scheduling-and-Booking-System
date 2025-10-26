package com.company.bus_mgmt.service_impl.schedule;

import com.company.bus_mgmt.domain.schedule.RouteStop;
import com.company.bus_mgmt.domain.schedule.RouteStopId;
import com.company.bus_mgmt.domain.schedule.Stop;
import com.company.bus_mgmt.exception.NotFoundException;
import com.company.bus_mgmt.repository.schedule.RouteRepository;
import com.company.bus_mgmt.repository.schedule.RouteStopRepository;
import com.company.bus_mgmt.repository.schedule.StopRepository;
import com.company.bus_mgmt.service.schedule.StopService;
import com.company.bus_mgmt.web.dto.stop.StopCreateToRouteRequest;
import com.company.bus_mgmt.web.dto.stop.StopResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StopServiceImpl implements StopService {

    private final StopRepository stops;
    private final RouteRepository routes;
    private final RouteStopRepository routeStops;

    public StopServiceImpl(StopRepository stops, RouteRepository routes, RouteStopRepository routeStops) {
        this.stops = stops; this.routes = routes; this.routeStops = routeStops;
    }

    @Override
    @Transactional
    public StopResponse createAndAttach(StopCreateToRouteRequest req) {
        var route = routes.findByNameIgnoreCase(req.routeName())
                .orElseThrow(() -> new NotFoundException("Route not found by name: " + req.routeName()));

        // create or reuse stop by name
        var stop = stops.findByNameIgnoreCase(req.stopName())
                .orElseGet(() -> {
                    Stop s = new Stop();
                    s.setName(req.stopName());
                    s.setLat(0.0); s.setLng(0.0); // coords optional now
                    return stops.save(s);
                });

        RouteStopId key = new RouteStopId(route.getId(), stop.getId());

        var existing = routeStops.findById(key).orElse(null);
        if (existing != null) {
            existing.setStopOrder(req.stopOrder());
            existing.setArrivalOffsetMin(req.arrivalOffsetMin());
            routeStops.save(existing); // merge/update
            return StopResponse.from(stop);
        }

        var rs = new RouteStop();

        rs.setRoute(route);
        rs.setStop(stop);
        rs.setStopOrder(req.stopOrder());
        rs.setArrivalOffsetMin(req.arrivalOffsetMin());
        routeStops.save(rs);

        return StopResponse.from(stop);
    }

    @Override
    public List<StopResponse> search(String q) {
        var list = (q == null || q.isBlank()) ? stops.findAll() : stops.findByNameContainingIgnoreCase(q);
        return list.stream().map(StopResponse::from).toList();
    }

    @Override
    public StopResponse rename(Long stopId, String newName) {
        var s = stops.findById(stopId).orElseThrow(() -> new NotFoundException("Stop not found"));
        s.setName(newName);
        stops.save(s);
        return StopResponse.from(s);
    }

    @Override
    public void delete(Long stopId) {
        // optionally: check if used in route_stops and prevent delete
        // long count = routeStops.countByStopId(stopId); (if you add such method)
        // if (count>0) throw new ConflictException("Stop is attached to routes");
        stops.deleteById(stopId);
    }

}
