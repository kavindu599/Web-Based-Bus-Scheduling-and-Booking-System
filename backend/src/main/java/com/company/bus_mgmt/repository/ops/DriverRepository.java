package com.company.bus_mgmt.repository.ops;

import com.company.bus_mgmt.domain.ops.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String first, String last);
}
