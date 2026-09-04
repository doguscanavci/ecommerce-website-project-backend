package com.bandage.ecommerce.controller;

import com.bandage.ecommerce.dto.request.LoginRequest;
import com.bandage.ecommerce.dto.request.SignUpRequest;
import com.bandage.ecommerce.dto.response.AuthResponse;
import com.bandage.ecommerce.security.UserPrincipal;
import com.bandage.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signUp(@Valid @RequestBody SignUpRequest request) {
        authService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Kayıt başarılı! Giriş yapabilirsiniz."));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/verify")
    public ResponseEntity<AuthResponse> verify(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(authService.verify(principal.getUsername()));
    }
}