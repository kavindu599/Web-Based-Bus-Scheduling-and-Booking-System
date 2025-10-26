package com.company.bus_mgmt.repository.schedule;

import com.company.bus_mgmt.domain.schedule.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TripRepository extends JpaRepository<Trip, Long> {
    Page<Trip> findByRoute_NameContainingIgnoreCaseAndDepartureTimeBetween(
            String routeName, LocalDateTime from, LocalDateTime to, Pageable pg);
    boolean existsByRoute_NameIgnoreCaseAndDepartureTimeAndArrivalTime(
            String routeName, LocalDateTime departureTime, LocalDateTime arrivalTime);
}
