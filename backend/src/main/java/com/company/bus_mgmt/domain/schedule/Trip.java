package com.company.bus_mgmt.domain.schedule;

import com.company.bus_mgmt.domain.ops.Bus;
import com.company.bus_mgmt.domain.ops.Driver;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
public class Trip {
    // getters & setters
    @Getter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @ManyToOne(optional = false) @JoinColumn(name = "route_id")
    private Route route;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "trip_type", nullable = false, length = 16)
    private TripType tripType = TripType.ONE_TIME;     // <— FIX: stored properly

    @Getter
    @Setter
    @Column(name = "publish_at")
    private LocalDateTime publishAt;                   // only used for ONE_TIME

    @Setter
    @Getter
    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Setter
    @Getter
    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Getter
    @Setter
    @Column(nullable = false, length = 24)
    private String status = "SCHEDULED";               // SCHEDULED|IN_PROGRESS|COMPLETED|CANCELLED|INACTIVE

    @Setter
    @Getter
    @ManyToOne @JoinColumn(name = "bus_id")
    private Bus bus;

    @Setter
    @Getter
    @ManyToOne @JoinColumn(name = "driver_id")
    private Driver driver;

    @Setter
    @Getter
    @Column(name = "reserved_seats", nullable = false)
    private Integer reservedSeats = 0;

    @Getter
    @Setter
    @Column(name = "active", nullable = false)
    private boolean active = true;                     // for SCHEDULED repeat flag

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

}
