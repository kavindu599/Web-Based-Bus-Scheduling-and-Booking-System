package com.company.bus_mgmt.web.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicRegisterRequest(
        @Email @NotBlank String email,
        @NotBlank @Size(min=3,max=120) String fullName,
        @NotBlank @Size(min=8) String password,
        @Size(max=32) String phone
) {}
