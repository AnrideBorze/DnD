package com.anrideborze.demo.controllers;

import com.anrideborze.demo.dto.RegisterRequest;
import com.anrideborze.demo.entities.User;
import com.anrideborze.demo.enums.Role;
import com.anrideborze.demo.repositories.UserRepository;
import com.anrideborze.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRole(Role.USER); // Додаємо роль USER за замовчуванням
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

}
