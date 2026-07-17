package com.example.galileo.feature.auth;

import java.time.Instant;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.example.galileo.config.JwtTokenProvider;
import com.example.galileo.feature.auth.dto.AuthResponse;
import com.example.galileo.feature.auth.dto.LoginRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private TokenBlacklistService blacklistService;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldReturnAuthResponseWhenLogin() {
        LoginRequest request = new LoginRequest("user@example.com", "password");
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("jwt-token");
        when(jwtTokenProvider.getExpirationMillis()).thenReturn(3600000L);

        AuthResponse response = authService.login(request);

        assertThat(response.token()).isEqualTo("jwt-token");
        assertThat(response.type()).isEqualTo("Bearer");
        assertThat(response.expiresInMillis()).isEqualTo(3600000L);
    }

    @Test
    void shouldBlacklistTokenWhenLogout() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Instant expiration = Instant.now().plusSeconds(3600);

        when(request.getHeader("Authorization")).thenReturn("Bearer jwt-token");
        when(jwtTokenProvider.validateToken("jwt-token")).thenReturn(true);
        when(jwtTokenProvider.getTokenId("jwt-token")).thenReturn("token-id");
        when(jwtTokenProvider.getExpiration("jwt-token")).thenReturn(expiration);

        authService.logout(request);

        verify(blacklistService).blacklistToken("token-id", expiration);
    }

    @Test
    void shouldThrowWhenLogoutWithInvalidToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
        when(jwtTokenProvider.validateToken("invalid-token")).thenReturn(false);

        assertThatThrownBy(() -> authService.logout(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Token inválido o ausente");
    }
}
