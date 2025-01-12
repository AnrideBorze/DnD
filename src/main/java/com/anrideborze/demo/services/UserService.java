package com.anrideborze.demo.services;

import com.anrideborze.demo.dto.RegisterRequest;
import com.anrideborze.demo.entities.User;
import com.anrideborze.demo.enums.Role;
import com.anrideborze.demo.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterRequest registerRequest) {
        // Перевірка унікальності
        if (userRepository.existsByUsername(registerRequest.getUsername()) ||
                userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Username or email already exists!");
        }

        // Створення користувача
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());

        // Призначення ролі (за замовчуванням USER, якщо роль не вказана)
        Role roleEnum = registerRequest.getRole() != null
                ? registerRequest.getRole() // Оскільки це вже тип Role, додаткове перетворення не потрібне
                : Role.USER; // Роль за замовчуванням

        user.setRoles(new HashSet<>(Set.of(roleEnum)));

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
