package com.company.bus_mgmt.service.user;

import com.company.bus_mgmt.web.dto.auth.LoginRequest;
import com.company.bus_mgmt.web.dto.auth.LoginResponse;
import com.company.bus_mgmt.web.dto.auth.PublicRegisterRequest;
import com.company.bus_mgmt.web.dto.user.UserCreateRequest;
import com.company.bus_mgmt.web.dto.user.UserResponse;

public interface AuthService {
    UserResponse register(UserCreateRequest req);
    UserResponse registerPassenger(PublicRegisterRequest req);
    LoginResponse login(LoginRequest req);
    LoginResponse refresh(String refreshToken);
}
