package com.company.bus_mgmt.web.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record SetRolesRequest(
        @NotNull @Size(min=1)
        List<@Pattern(regexp="ADMIN|OPS_MANAGER|BOOKING_CLERK|CUSTOMER_SERVICE|SENIOR_TICKETING|DRIVER|IT_TECH|PASSENGER")
                String> roles
) {}
