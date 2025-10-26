package com.company.bus_mgmt.domain.schedule;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Entity
@Table(name="routes")
public class Route {
    // getters/setters
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable=false, unique=true, length=120)
    private String name;

    @Setter
    @Column(nullable=false, length=120)
    private String origin;

    @Setter
    @Column(nullable=false, length=120)
    private String destination;

    @Setter
    @Column(name="distance_km", nullable=false)
    private Double distanceKm = 0.0;

    @Setter
    @Column(name="duration_min", nullable=false)
    private Integer durationMin;

    @Setter
    @Column(nullable=false, length=24)
    private String status = "ACTIVE";

    @Column(name="created_at", nullable=false)
    private Instant createdAt = Instant.now();

}
