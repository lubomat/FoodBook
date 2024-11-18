package com.cookBook.CookBook.controller;

import com.cookBook.CookBook.model.User;
import com.cookBook.CookBook.security.AuthenticationRequest;
import com.cookBook.CookBook.security.AuthenticationResponse;
import com.cookBook.CookBook.security.JwtUtil;
import com.cookBook.CookBook.service.UserService;
import com.cookBook.CookBook.utils.ValidationUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
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

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            logger.warn("Registration failed: Password and Confirm Password do not match.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match.");
        }

        if (ValidationUtils.containsPolishCharacters(user.getUsername())) {
            logger.warn("Registration failed: Username '{}' contains Polish characters.", user.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username cannot contain Polish characters.");
        }

        if (ValidationUtils.containsForbiddenWords(user.getUsername())) {
            logger.warn("Registration failed: Username '{}' contains forbidden words.", user.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username contains forbidden words and is not allowed.");
        }

        if (userService.findByUsername(user.getUsername()).isPresent()) {
            logger.warn("Registration failed: Username '{}' already exists.", user.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username already exists.");
        }

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            logger.warn("Registration failed: Email '{}' already exists.", user.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email already exists.");
        }

        try {
            userService.saveUser(user);
        } catch (RuntimeException ex) {
            logger.error("Registration failed due to internal error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again later.");
        }

        logger.info("User '{}' registered successfully.", user.getUsername());
        return ResponseEntity.ok("User registered successfully.");
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
            throw new RuntimeException("Invalid credentials");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsernameOrEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        logger.info("JWT token generated for user: {}", userDetails.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
