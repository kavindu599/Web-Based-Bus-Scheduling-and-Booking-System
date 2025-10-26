package com.company.bus_mgmt.domain.schedule;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Entity
@Table(name = "stops", indexes = {
        @Index(name = "ix_stop_name", columnList = "name")
})
public class Stop {

    // getters/setters
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, length = 160)
    private String name;

    // Coordinates are optional in your current flow
    @Setter
    @Column
    private Double lat;

    @Setter
    @Column
    private Double lng;

    @Setter
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

}
