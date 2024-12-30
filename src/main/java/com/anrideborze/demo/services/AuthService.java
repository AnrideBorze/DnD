package com.anrideborze.demo.services;

import com.anrideborze.demo.dto.LoginRequest;
import com.anrideborze.demo.entities.User;
import com.anrideborze.demo.enums.Role;
import com.anrideborze.demo.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


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

    public Map<String, Object> login(LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        if (userOpt.isEmpty() || !passwordEncoder.matches(loginRequest.getPassword(), userOpt.get().getPassword())) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        User user = userOpt.get();

        // Створення JWT токену
        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getRoles()) // Додаємо ролі
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 день
                .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS512)) // Генерація підпису
                .compact();

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("message", "Login successful.");
        return response;
    }
}
