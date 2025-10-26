package com.company.bus_mgmt.web.controller.auth;

import com.company.bus_mgmt.service.user.AuthService;
import com.company.bus_mgmt.web.dto.auth.LoginRequest;
import com.company.bus_mgmt.web.dto.auth.LoginResponse;
import com.company.bus_mgmt.web.dto.auth.PublicRegisterRequest;
import com.company.bus_mgmt.web.dto.user.UserCreateRequest;
import com.company.bus_mgmt.web.dto.user.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping("/public/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse publicRegister(@Valid @RequestBody PublicRegisterRequest req) {
        return auth.registerPassenger(req);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody UserCreateRequest req){
        return auth.register(req);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req){
        return auth.login(req);
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(@RequestBody RefreshTokenInput input){
        return auth.refresh(input.refreshToken());
    }

    public record RefreshTokenInput(String refreshToken) {}
}
