package com.company.bus_mgmt.web.dto.user;

import jakarta.validation.constraints.*;
import java.util.List;

public record UserCreateRequest(
        @Email @NotBlank String email,
        @NotBlank @Size(min=3,max=120) String fullName,
        @NotBlank @Size(min=8) String password,
        @Size(max=32) String phone,
        @NotNull @Size(min=1) List<@Pattern(regexp="ADMIN|OPS_MANAGER|BOOKING_CLERK|CUSTOMER_SERVICE|SENIOR_TICKETING|DRIVER|IT_TECH") String> roles
) {}
