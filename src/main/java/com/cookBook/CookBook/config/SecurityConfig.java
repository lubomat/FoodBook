package com.cookBook.CookBook.config;
import com.cookBook.CookBook.security.JwtRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final JwtRequestFilter jwtRequestFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, UserDetailsService userDetailsService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.userDetailsService = userDetailsService;
        logger.info("Initializing SecurityConfig with JwtRequestFilter and UserDetailsService.");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring SecurityFilterChain.");
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/api/recipes", "/api/recipes/category/**", "/api/recipes/name/**", "/api/recipes/{id}", "/uploads/**","/api/comments/**").permitAll()
                        .requestMatchers("/api/recipes", "/api/recipes/**").hasRole("USER")
                        .requestMatchers("/api/comments","/api/comments/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        logger.info("SecurityFilterChain successfully configured.");

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Creating PasswordEncoder bean.");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        logger.info("Creating AuthenticationManager bean.");
        return authenticationConfiguration.getAuthenticationManager();
    }
}
