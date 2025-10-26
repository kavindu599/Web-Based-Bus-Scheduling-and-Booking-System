package com.company.bus_mgmt.repository.schedule;

import com.company.bus_mgmt.domain.schedule.Stop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StopRepository extends JpaRepository<Stop, Long> {
    List<Stop> findByNameContainingIgnoreCase(String q);
    Optional<Stop> findByNameIgnoreCase(String name);
}
