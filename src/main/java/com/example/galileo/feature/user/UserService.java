package com.example.galileo.feature.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.galileo.exception.DuplicateResourceException;
import com.example.galileo.exception.ResourceNotFoundException;
import com.example.galileo.feature.user.dto.UserRequest;
import com.example.galileo.feature.user.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Page<User> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    @Transactional
    public User create(UserRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already in use: " + request.email());
        }

        User user = new User(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password()),
                UserRole.USER
        );
        return repository.save(user);
    }

    @Transactional
    public User update(Long id, UserRequest request) {
        if (repository.existsByEmailAndIdNot(request.email(), id)) {
            throw new DuplicateResourceException("Email already in use: " + request.email());
        }

        User user = findById(id);
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        return repository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        User user = findById(id);
        repository.delete(user);
    }
}
