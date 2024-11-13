package com.cookBook.CookBook.controller;

import com.cookBook.CookBook.model.User;
import com.cookBook.CookBook.security.AuthenticationRequest;
import com.cookBook.CookBook.security.AuthenticationResponse;
import com.cookBook.CookBook.security.JwtUtil;
import com.cookBook.CookBook.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        logger.info("Received registration request for username: {}", user.getUsername());

        if (userService.findByUsername(user.getUsername()).isPresent()) {
            logger.warn("Registration failed: Username '{}' already exists.", user.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            logger.warn("Registration failed: Email '{}' already exists.", user.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }
        userService.saveUser(user);
        logger.info("User '{}' registered successfully.", user.getUsername());
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest request) throws Exception {
        logger.info("Login attempt for user/email: {}", request.getUsernameOrEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword())
            );
            logger.info("Authentication successful for user/email: {}", request.getUsernameOrEmail());
        } catch (Exception e) {
            logger.error("Authentication failed for user/email: {}. Reason: {}", request.getUsernameOrEmail(), e.getMessage());
            throw new RuntimeException("Nieprawidłowe dane uwierzytelniające");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsernameOrEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        logger.info("JWT token generated for user: {}", userDetails.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
