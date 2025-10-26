package com.company.bus_mgmt.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name="users")
public class User {
    // getters/setters
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable=false, unique=true, length=160)
    private String email;

    @Setter
    @Column(name="password_hash", nullable=false, length=255)
    private String passwordHash;

    @Setter
    @Column(name="full_name", nullable=false, length=120)
    private String fullName;

    @Setter
    @Column(length=32)
    private String phone;

    @Setter
    @Column(nullable=false, length=24)
    private String status = "ACTIVE";

    @Column(name="created_at", nullable=false)
    private Instant createdAt = Instant.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_roles",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();

}
