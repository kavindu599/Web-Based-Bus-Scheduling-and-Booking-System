package com.company.bus_mgmt.repository.schedule;

import com.company.bus_mgmt.domain.schedule.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {
    Page<Route> findByNameContainingIgnoreCaseOrOriginContainingIgnoreCaseOrDestinationContainingIgnoreCase(
            String name, String origin, String destination, Pageable pageable);
    Optional<Route> findByNameIgnoreCase(String name);
}
