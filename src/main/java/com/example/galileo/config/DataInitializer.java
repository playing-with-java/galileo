package com.example.galileo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.example.galileo.feature.user.User;
import com.example.galileo.feature.user.UserRepository;
import com.example.galileo.feature.user.UserRole;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User(
                    "Administrator",
                    "admin@example.com",
                    passwordEncoder.encode("Admin123!"),
                    UserRole.ADMIN
            );
            userRepository.save(admin);
        }
    }
}
