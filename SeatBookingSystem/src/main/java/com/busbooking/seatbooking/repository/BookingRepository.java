package com.busbooking.seatbooking.repository;

import com.busbooking.seatbooking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsBySeatNumberAndBusNumber(Integer seatNumber, String busNumber);
    List<Booking> findByStatus(String status);

    @Modifying
    @Transactional
    @Query("update Booking b set b.status = :status where b.id = :id")
    int updateStatusById(@Param("id") Long id, @Param("status") String status);
}

