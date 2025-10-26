package com.company.bus_mgmt.repository.user;

import com.company.bus_mgmt.domain.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    @Query("select r from Role r where upper(trim(r.name)) = upper(?1)")
    Optional<Role> findByNameNormalized(String name);
}
