package com.company.bus_mgmt.web.dto.user;

import com.company.bus_mgmt.domain.user.User;

import java.time.Instant;
import java.util.List;

public record UserResponse(Long id, String email, String fullName, String phone, String status,
                           List<RoleRef> roles, Instant createdAt) {
    public record RoleRef(Long id, String name) {}
    public static UserResponse from(User u){
        List<RoleRef> rs = u.getRoles().stream().map(r -> new RoleRef(r.getId(), r.getName())).toList();
        return new UserResponse(u.getId(), u.getEmail(), u.getFullName(), u.getPhone(), u.getStatus(), rs, u.getCreatedAt());
    }
}
