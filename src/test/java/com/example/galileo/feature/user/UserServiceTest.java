package com.example.galileo.feature.user;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.galileo.exception.DuplicateResourceException;
import com.example.galileo.exception.ResourceNotFoundException;
import com.example.galileo.feature.user.dto.UserRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnPageWhenFindAll() {
        User user = new User("Alice", "alice@example.com", "encoded", UserRole.USER);
        Page<User> page = new PageImpl<>(List.of(user));
        Pageable pageable = PageRequest.of(0, 20);

        when(repository.findAll(pageable)).thenReturn(page);

        Page<User> result = userService.findAll(pageable);

        assertThat(result.getContent()).containsExactly(user);
    }

    @Test
    void shouldReturnUserWhenFoundById() {
        User user = new User("Alice", "alice@example.com", "encoded", UserRole.USER);
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with id 1");
    }

    @Test
    void shouldCreateUserWhenEmailDoesNotExist() {
        UserRequest request = new UserRequest("Alice", "alice@example.com", "password");
        when(repository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded-password");
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.create(request);

        assertThat(result.getName()).isEqualTo("Alice");
        assertThat(result.getEmail()).isEqualTo("alice@example.com");
        assertThat(result.getPassword()).isEqualTo("encoded-password");
        assertThat(result.getRole()).isEqualTo(UserRole.USER);
        verify(repository).save(result);
    }

    @Test
    void shouldThrowWhenCreateUserWithDuplicateEmail() {
        UserRequest request = new UserRequest("Alice", "alice@example.com", "password");
        when(repository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email already in use: alice@example.com");
    }

    @Test
    void shouldUpdateUserWhenEmailIsAvailable() {
        UserRequest request = new UserRequest("Alice Updated", "alice@example.com", "new-password");
        User existing = new User("Alice", "alice@example.com", "old-password", UserRole.USER);
        when(repository.existsByEmailAndIdNot(request.email(), 1L)).thenReturn(false);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode(request.password())).thenReturn("new-encoded");
        when(repository.save(existing)).thenReturn(existing);

        User result = userService.update(1L, request);

        assertThat(result.getName()).isEqualTo("Alice Updated");
        assertThat(result.getEmail()).isEqualTo("alice@example.com");
        assertThat(result.getPassword()).isEqualTo("new-encoded");
        verify(repository).save(existing);
    }

    @Test
    void shouldThrowWhenUpdateUserWithDuplicateEmail() {
        UserRequest request = new UserRequest("Alice Updated", "alice@example.com", "new-password");
        when(repository.existsByEmailAndIdNot(request.email(), 1L)).thenReturn(true);

        assertThatThrownBy(() -> userService.update(1L, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email already in use: alice@example.com");
    }

    @Test
    void shouldDeleteExistingUser() {
        User existing = new User("Alice", "alice@example.com", "encoded", UserRole.USER);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        userService.delete(1L);

        verify(repository).delete(existing);
    }
}
