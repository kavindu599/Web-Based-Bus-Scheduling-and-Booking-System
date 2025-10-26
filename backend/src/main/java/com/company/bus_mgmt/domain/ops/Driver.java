package com.company.bus_mgmt.domain.ops;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Entity
@Table(name = "drivers", indexes = {
        @Index(name = "ix_driver_license", columnList = "license_no", unique = true)
})
public class Driver {

    // getters/setters
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;

    @Setter
    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;

    @Setter
    @Column(name = "license_no", nullable = false, unique = true, length = 64)
    private String licenseNo;

    @Setter
    @Column(length = 32)
    private String phone;

    @Setter
    @Column(nullable = false, length = 24)
    private String status = "ACTIVE"; // ACTIVE | INACTIVE | SUSPENDED

    @Setter
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

}
