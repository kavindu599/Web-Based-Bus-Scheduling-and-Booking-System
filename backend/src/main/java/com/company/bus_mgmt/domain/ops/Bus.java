package com.company.bus_mgmt.domain.ops;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Entity
@Table(name = "buses", indexes = {
        @Index(name = "ix_bus_plate", columnList = "plate_number", unique = true)
})
public class Bus {

    // getters/setters
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "plate_number", nullable = false, unique = true, length = 64)
    private String plateNumber;

    @Setter
    @Column(nullable = false)
    private Integer capacity;

    @Setter
    @Column(nullable = false, length = 40)
    private String type;            // e.g., "AC", "NON_AC", "MINI"

    @Setter
    @Column(nullable = false, length = 24)
    private String status = "ACTIVE"; // ACTIVE | INACTIVE | MAINTENANCE

    @Setter
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

}
