package com.company.bus_mgmt.web.dto.auth;

import com.company.bus_mgmt.web.dto.user.UserResponse;

public record LoginResponse(String accessToken, String refreshToken, UserResponse user) {
    public static LoginResponse of(String a, String r, UserResponse u){ return new LoginResponse(a,r,u); }
}
