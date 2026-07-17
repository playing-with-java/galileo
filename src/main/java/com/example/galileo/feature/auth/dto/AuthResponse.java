package com.example.galileo.feature.auth.dto;

public record AuthResponse(String token, String type, long expiresInMillis) {
}
