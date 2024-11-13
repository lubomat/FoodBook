package com.cookBook.CookBook.service;

import com.cookBook.CookBook.model.User;
import com.cookBook.CookBook.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        logger.info("Attempting to load user by username or email: {}", usernameOrEmail);

        User user = userRepository.findByUsername(usernameOrEmail)
                .orElseGet(() -> {
                    logger.info("User not found by username: {}. Attempting to load by email.", usernameOrEmail);
                    return userRepository.findByEmail(usernameOrEmail)
                            .orElseThrow(() -> {
                                logger.error("User not found by username or email: {}", usernameOrEmail);
                                return new UsernameNotFoundException("User or email address not found: " + usernameOrEmail);
                            });
                });

        logger.info("User successfully loaded: {}", user.getUsername());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}
