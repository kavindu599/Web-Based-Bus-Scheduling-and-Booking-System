package com.company.bus_mgmt.service_impl.ops;

import com.company.bus_mgmt.domain.schedule.Trip;
import com.company.bus_mgmt.exception.ConflictException;
import com.company.bus_mgmt.exception.NotFoundException;
import com.company.bus_mgmt.repository.ops.BusRepository;
import com.company.bus_mgmt.repository.ops.DriverRepository;
import com.company.bus_mgmt.repository.schedule.TripRepository;
import com.company.bus_mgmt.service.ops.AssignmentService;
import com.company.bus_mgmt.web.dto.assignment.AssignmentByLookupRequest;
import com.company.bus_mgmt.web.dto.trip.TripResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final TripRepository trips;
    private final BusRepository buses;
    private final DriverRepository drivers;

    public AssignmentServiceImpl(TripRepository trips, BusRepository buses, DriverRepository drivers) {
        this.trips = trips; this.buses = buses; this.drivers = drivers;
    }

    @Override
    public TripResponse assignByLookup(AssignmentByLookupRequest req) {
        Trip t = trips.findById(req.tripId()).orElseThrow(() -> new NotFoundException("Trip not found"));

        var bus = buses.findByPlateNumberIgnoreCase(req.busPlate())
                .or(() -> {
                    var list = buses.findByPlateNumberContainingIgnoreCase(req.busPlate());
                    if (list.size() == 1) return java.util.Optional.of(list.get(0));
                    if (list.isEmpty()) throw new NotFoundException("Bus not found by plate");
                    throw new ConflictException("Multiple buses match the plate; be more specific");
                }).orElseThrow();

        var parts = req.driverName().trim().split("\\s+");
        String p1 = parts[0], p2 = parts.length > 1 ? parts[1] : parts[0];
        var ds = drivers.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(p1, p2);
        if (ds.isEmpty()) throw new NotFoundException("Driver not found by name");
        if (ds.size() > 1) throw new ConflictException("Multiple drivers match that name; be more specific");

        t.setBus(bus);
        t.setDriver(ds.get(0));
        trips.save(t);
        return TripResponse.from(t);
    }

    @Override
    public List<String> availableBuses() {
        return buses.findAll().stream()
                .map(b -> b.getPlateNumber() + " (" + b.getCapacity() + ")")
                .toList();
    }

    @Override
    public List<String> availableDrivers() {
        return drivers.findAll().stream()
                .map(d -> d.getFirstName() + " " + d.getLastName() + " (id:" + d.getId() + ")")
                .toList();
    }
}
