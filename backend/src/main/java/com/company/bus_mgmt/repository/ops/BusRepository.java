package com.company.bus_mgmt.repository.ops;

import com.company.bus_mgmt.domain.ops.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusRepository extends JpaRepository<Bus, Long> {
    Optional<Bus> findByPlateNumberIgnoreCase(String plateNumber);
    List<Bus> findByPlateNumberContainingIgnoreCase(String q);
}
