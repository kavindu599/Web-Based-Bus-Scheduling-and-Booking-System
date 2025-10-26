package com.company.bus_mgmt.service_impl.ops;

import com.company.bus_mgmt.domain.schedule.Trip;
import com.company.bus_mgmt.domain.schedule.TripType;
import com.company.bus_mgmt.exception.ConflictException;
import com.company.bus_mgmt.exception.NotFoundException;
import com.company.bus_mgmt.repository.schedule.RouteRepository;
import com.company.bus_mgmt.repository.schedule.TripRepository;
import com.company.bus_mgmt.service.ops.TripService;
import com.company.bus_mgmt.web.dto.trip.TripCompactItem;
import com.company.bus_mgmt.web.dto.trip.TripCreateRequest;
import com.company.bus_mgmt.web.dto.trip.TripResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TripServiceImpl implements TripService {

    private final TripRepository trips;
    private final RouteRepository routes;

    public TripServiceImpl(TripRepository trips, RouteRepository routes) {
        this.trips = trips; this.routes = routes;
    }

    @Override
    public TripResponse create(TripCreateRequest req) {
        var route = routes.findByNameIgnoreCase(req.routeName())
                .orElseThrow(() -> new NotFoundException("Route not found by name: " + req.routeName()));

        // Base validations common to both
        if (!req.arrivalTime().isAfter(req.departureTime()))
            throw new ConflictException("arrivalTime must be greater than departureTime");

        boolean exists = trips.existsByRoute_NameIgnoreCaseAndDepartureTimeAndArrivalTime(
                route.getName(), req.departureTime(), req.arrivalTime());
        if (exists) {
            throw new ConflictException(
                    "A trip already exists for route '%s' at %s → %s"
                            .formatted(route.getName(), req.departureTime(), req.arrivalTime())
            );
        }


        Trip t = new Trip();
        t.setRoute(route);
        t.setDepartureTime(req.departureTime());
        t.setArrivalTime(req.arrivalTime());

        if ("ONE_TIME".equalsIgnoreCase(req.tripType())) {
            t.setTripType(TripType.ONE_TIME);

            if (req.publishAt() == null)
                throw new ConflictException("publishAt is required for ONE_TIME trips");
            if (req.publishAt().isBefore(LocalDateTime.now()))
                throw new ConflictException("publishAt cannot be in the past");
            if (req.publishAt().isAfter(req.departureTime()))
                throw new ConflictException("publishAt must be before or equal to departureTime");

            t.setPublishAt(req.publishAt());
            t.setActive(false);            // one-time is not a repeating schedule
            t.setStatus("SCHEDULED");      // will be visible at publishAt on UI; API already stores it
        } else {
            t.setTripType(TripType.SCHEDULED);
            t.setPublishAt(null);          // not applicable
            t.setActive(true);             // repeats until deactivated (flag)
            t.setStatus("SCHEDULED");
        }

        trips.save(t);
        return TripResponse.from(t);
    }

    @Override
    public TripResponse get(Long id) {
        var t = trips.findById(id).orElseThrow(() -> new NotFoundException("Trip not found"));
        return TripResponse.from(t);
    }

    @Override
    public Page<TripResponse> search(String routeName, LocalDateTime from, LocalDateTime to, Pageable pg) {
        var page = trips.findByRoute_NameContainingIgnoreCaseAndDepartureTimeBetween(
                routeName == null ? "" : routeName,
                from == null ? LocalDateTime.MIN : from,
                to == null ? LocalDateTime.MAX : to,
                pg);
        return page.map(TripResponse::from);
    }

    @Override
    public List<TripCompactItem> compact(String routeName, LocalDateTime from, LocalDateTime to, int limit) {
        var page = search(routeName, from, to, Pageable.ofSize(Math.max(1, limit)));
        return page.getContent().stream().map(TripCompactItem::fromResponse).toList();
    }

    @Override
    public void deactivate(Long id) {
        var t = trips.findById(id).orElseThrow(() -> new NotFoundException("Trip not found"));
        if (t.getTripType() == TripType.SCHEDULED) {
            t.setActive(false);
        } else {
            t.setStatus("INACTIVE");
        }
        trips.save(t);
    }

    @Override
    public void activate(Long id) {
        var t = trips.findById(id).orElseThrow(() -> new NotFoundException("Trip not found"));
        if (t.getTripType() == TripType.SCHEDULED) {
            t.setActive(true);
            trips.save(t);
        } else {
            throw new ConflictException("Only SCHEDULED trips can be activated");
        }
    }

    @Override
    public void completeIfPast(Long id) {
        var t = trips.findById(id).orElseThrow(() -> new NotFoundException("Trip not found"));
        if (t.getArrivalTime().isBefore(LocalDateTime.now()) && !"COMPLETED".equals(t.getStatus())) {
            t.setStatus("COMPLETED");
            trips.save(t);
        }
    }

    @Override
    public TripResponse update(Long id, com.company.bus_mgmt.web.dto.trip.TripUpdateRequest req) {
        var t = trips.findById(id).orElseThrow(() -> new com.company.bus_mgmt.exception.NotFoundException("Trip not found"));

        if (req.routeName()!=null && !req.routeName().isBlank()) {
            var route = routes.findByNameIgnoreCase(req.routeName())
                    .orElseThrow(() -> new com.company.bus_mgmt.exception.NotFoundException("Route not found by name: "+req.routeName()));
            t.setRoute(route);
        }

        if (req.tripType()!=null) {
            var newType = com.company.bus_mgmt.domain.schedule.TripType.from(req.tripType());
            t.setTripType(newType);
            if (newType == com.company.bus_mgmt.domain.schedule.TripType.ONE_TIME) {
                if (req.publishAt()==null && t.getPublishAt()==null)
                    throw new com.company.bus_mgmt.exception.ConflictException("publishAt required for ONE_TIME trips");
                if (req.publishAt()!=null) {
                    if (req.publishAt().isBefore(java.time.LocalDateTime.now()))
                        throw new com.company.bus_mgmt.exception.ConflictException("publishAt cannot be in the past");
                    t.setPublishAt(req.publishAt());
                }
                t.setActive(false);
            } else {
                t.setPublishAt(null);
                t.setActive(req.active()!=null ? req.active() : true);
            }
        } else if (t.getTripType()==com.company.bus_mgmt.domain.schedule.TripType.ONE_TIME && req.publishAt()!=null) {
            if (req.publishAt().isBefore(java.time.LocalDateTime.now()))
                throw new com.company.bus_mgmt.exception.ConflictException("publishAt cannot be in the past");
            t.setPublishAt(req.publishAt());
        }

        var dep = req.departureTime()!=null ? req.departureTime() : t.getDepartureTime();
        var arr = req.arrivalTime()!=null   ? req.arrivalTime()   : t.getArrivalTime();
        if (!arr.isAfter(dep))
            throw new com.company.bus_mgmt.exception.ConflictException("arrivalTime must be greater than departureTime");
        t.setDepartureTime(dep);
        t.setArrivalTime(arr);

        if (req.status()!=null) t.setStatus(req.status());

        trips.save(t);
        return com.company.bus_mgmt.web.dto.trip.TripResponse.from(t);
    }

    @Override
    public void delete(Long id) {
        if (!trips.existsById(id)) {
            throw new com.company.bus_mgmt.exception.NotFoundException("Trip not found");
        }
        trips.deleteById(id);
    }


}
