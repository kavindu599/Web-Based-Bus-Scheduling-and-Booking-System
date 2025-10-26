package com.company.bus_mgmt.web.dto.user;

import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(min=3,max=120) String fullName,
        @Size(max=32) String phone
) {}
