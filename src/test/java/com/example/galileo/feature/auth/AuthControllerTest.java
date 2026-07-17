package com.example.galileo.feature.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.galileo.feature.auth.dto.AuthResponse;
import com.example.galileo.feature.auth.dto.LoginRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void shouldReturnAuthResponseWhenLogin() {
        LoginRequest request = new LoginRequest("user@example.com", "password");
        AuthResponse expected = new AuthResponse("token", "Bearer", 3600000L);

        when(authService.login(request)).thenReturn(expected);

        ResponseEntity<AuthResponse> response = authController.login(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expected);
        verify(authService).login(request);
    }

    @Test
    void shouldReturnNoContentWhenLogout() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        ResponseEntity<Void> response = authController.logout(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(authService).logout(request);
    }
}
