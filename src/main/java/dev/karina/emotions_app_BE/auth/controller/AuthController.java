package dev.karina.emotions_app_BE.auth.controller;

import dev.karina.emotions_app_BE.auth.dto.AuthResponse;
import dev.karina.emotions_app_BE.auth.dto.LoginRequest;
import dev.karina.emotions_app_BE.auth.dto.RegisterRequest;
import dev.karina.emotions_app_BE.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}