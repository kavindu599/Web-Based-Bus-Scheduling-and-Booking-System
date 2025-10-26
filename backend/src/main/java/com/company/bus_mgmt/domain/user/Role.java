package com.company.bus_mgmt.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Entity
@Table(name = "roles")
public class Role {
    // getters/setters
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable=false, unique=true, length=64)
    private String name;

    @Setter
    @Column(length=255)
    private String description;

    @Column(name="created_at", nullable=false)
    private Instant createdAt = Instant.now();

}
