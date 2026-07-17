package com.example.galileo.feature.user;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.galileo.feature.user.dto.UserRequest;
import com.example.galileo.feature.user.dto.UserResponse;
import com.example.galileo.feature.user.UserRole;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void shouldReturnUserPageWhenGetAll() {
        User user = new User("Alice", "alice@example.com", "secret", UserRole.USER);
        user.setId(1L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(List.of(user));

        when(userService.findAll(pageable)).thenReturn(page);

        Page<UserResponse> response = userController.getAll(pageable);

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).email()).isEqualTo("alice@example.com");
        verify(userService).findAll(pageable);
    }

    @Test
    void shouldReturnUserResponseWhenGetById() {
        User user = new User("Alice", "alice@example.com", "secret", UserRole.USER);
        user.setId(1L);

        when(userService.findById(1L)).thenReturn(user);

        UserResponse response = userController.getById(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("alice@example.com");
        verify(userService).findById(1L);
    }

    @Test
    void shouldCreateUserAndReturnCreatedResponse() {
        UserRequest request = new UserRequest("Alice", "alice@example.com", "password");
        User created = new User("Alice", "alice@example.com", "encoded", UserRole.USER);
        created.setId(1L);

        when(userService.create(request)).thenReturn(created);

        ResponseEntity<UserResponse> response = userController.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().email()).isEqualTo("alice@example.com");
        verify(userService).create(request);
    }

    @Test
    void shouldUpdateUserAndReturnUpdatedValue() {
        UserRequest request = new UserRequest("Alice", "alice@example.com", "new-password");
        User updated = new User("Alice", "alice@example.com", "encoded", UserRole.USER);
        updated.setId(2L);

        when(userService.update(2L, request)).thenReturn(updated);

        UserResponse response = userController.update(2L, request);

        assertThat(response.id()).isEqualTo(2L);
        assertThat(response.email()).isEqualTo("alice@example.com");
        verify(userService).update(2L, request);
    }

    @Test
    void shouldDeleteUserAndReturnNoContent() {
        ResponseEntity<Void> response = userController.delete(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(userService).delete(1L);
    }
}
