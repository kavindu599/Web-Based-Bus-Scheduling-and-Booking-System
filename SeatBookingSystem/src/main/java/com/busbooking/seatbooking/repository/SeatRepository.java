package com.busbooking.seatbooking.repository;


import com.busbooking.seatbooking.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByBusNumber(String busNumber);
    Seat findByBusNumberAndSeatNumber(String busNumber, Integer seatNumber);
}

