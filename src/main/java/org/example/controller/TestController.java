package org.example.controller;

import org.example.util.JwtAuthenticationUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/public")
    public ResponseEntity<Map<String, String>> publicEndpoint() {
        return ResponseEntity.ok(Map.of("message", "This is a public endpoint", "status", "success"));
    }

    @GetMapping("/protected")
    public ResponseEntity<Map<String, Object>> protectedEndpoint() {
        Long userId = JwtAuthenticationUtil.getCurrentUserId();
        String email = JwtAuthenticationUtil.getCurrentUserEmail();
        String role = JwtAuthenticationUtil.getCurrentUserRole();

        return ResponseEntity.ok(Map.of(
                "message", "This is a protected endpoint",
                "status", "success",
                "userId", userId != null ? userId : "null",
                "email", email != null ? email : "null",
                "role", role != null ? role : "null"));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Map<String, String>> adminEndpoint() {
        return ResponseEntity.ok(Map.of("message", "This is an admin-only endpoint", "status", "success"));
    }
}
