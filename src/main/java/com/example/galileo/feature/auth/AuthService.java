package com.example.galileo.feature.auth;

import java.time.Instant;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.galileo.feature.auth.dto.AuthResponse;
import com.example.galileo.feature.auth.dto.LoginRequest;
import com.example.galileo.config.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService blacklistService;

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        String token = jwtTokenProvider.generateToken(authentication);
        return new AuthResponse(token, "Bearer", jwtTokenProvider.getExpirationMillis());
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        String token = resolveToken(request);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("Token inválido o ausente");
        }

        String tokenId = jwtTokenProvider.getTokenId(token);
        Instant expiresAt = jwtTokenProvider.getExpiration(token);
        blacklistService.blacklistToken(tokenId, expiresAt);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
