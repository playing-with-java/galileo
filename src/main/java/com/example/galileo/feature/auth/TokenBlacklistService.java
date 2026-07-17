package com.example.galileo.feature.auth;

import java.time.Instant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenBlacklistService {

    private final BlacklistedTokenRepository repository;

    public void blacklistToken(String tokenId, Instant expiresAt) {
        if (repository.existsById(tokenId)) {
            return;
        }
        repository.save(new BlacklistedToken(tokenId, expiresAt));
    }

    public boolean isTokenBlacklisted(String tokenId) {
        return tokenId != null && repository.existsById(tokenId);
    }
}
