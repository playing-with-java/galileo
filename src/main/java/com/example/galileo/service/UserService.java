package com.example.galileo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.galileo.dto.UserRequest;
import com.example.galileo.exception.DuplicateResourceException;
import com.example.galileo.exception.ResourceNotFoundException;
import com.example.galileo.model.User;
import com.example.galileo.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

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

        User user = new User(request.name(), request.email());
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
        return repository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        User user = findById(id);
        repository.delete(user);
    }
}
