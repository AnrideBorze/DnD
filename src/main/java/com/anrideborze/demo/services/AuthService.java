package com.anrideborze.demo.services;

import com.anrideborze.demo.entities.User;
import com.anrideborze.demo.enums.Role;
import com.anrideborze.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRole(Role.USER); // Додаємо роль USER за замовчуванням
        return userRepository.save(user);
    }
}
