package com.saas.SlotBooker.controller;

import com.saas.SlotBooker.domain.auth.LoginRequest;
import com.saas.SlotBooker.domain.auth.LoginResponse;
import com.saas.SlotBooker.domain.auth.RegisterUserRequest;
import com.saas.SlotBooker.domain.auth.RegisterUserResponse;
import com.saas.SlotBooker.service.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.uri}/auth")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        RegisterUserResponse registerResponse = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }
}
