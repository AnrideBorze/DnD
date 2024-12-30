package com.anrideborze.demo.services;

import com.anrideborze.demo.dto.LoginRequest;
import com.anrideborze.demo.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public Map<String, Object> login(LoginRequest loginRequest) {
        Optional<User> userOpt = userService.findByUsername(loginRequest.getUsername());
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

