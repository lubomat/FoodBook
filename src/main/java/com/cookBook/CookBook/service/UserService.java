package com.cookBook.CookBook.service;

import com.cookBook.CookBook.model.User;
import com.cookBook.CookBook.repository.UserRepository;
import com.cookBook.CookBook.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        logger.info("Attempting to save user with email: {}", user.getEmail());

        if (ValidationUtils.containsPolishCharacters(user.getUsername())) {
            logger.error("Username {} contains Polish characters. Registration denied.", user.getUsername());
            throw new RuntimeException("Username contains Polish characters, which are not allowed.");
        }

        if (ValidationUtils.containsForbiddenWords(user.getUsername())) {
            logger.error("Username {} contains forbidden words. Registration denied.", user.getUsername());
            throw new RuntimeException("Username contains forbidden words.");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            logger.error("Email {} is already registered.", user.getEmail());
            throw new RuntimeException("Email already registered.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        logger.info("Password for user with email {} has been encrypted.", user.getEmail());

        User savedUser = userRepository.save(user);
        logger.info("User with email {} saved successfully with ID: {}", user.getEmail(), savedUser.getId());
        return savedUser;
    }


    public Optional<User> findByUsername(String username) {
        logger.info("Searching for user by username: {}", username);
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            logger.info("User with username {} found.", username);
        } else {
            logger.warn("User with username {} not found.", username);
        }

        return user;
    }

    public Optional<User> findByEmail(String email) {
        logger.info("Searching for user by email: {}", email);
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            logger.info("User with email {} found.", email);
        } else {
            logger.warn("User with email {} not found.", email);
        }

        return user;
    }
}
/*test*/