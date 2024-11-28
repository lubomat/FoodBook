package com.cookBook.CookBook.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserAuthTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String username;
    private String password;
    private String email;

    @BeforeEach
    public void setup() {
        int randomNum = (int) (Math.random() * (10000 - 10 + 1)) + 10;
        this.username = "test" + randomNum;
        this.password = "Test123!";
        this.email = "test" + randomNum + "@example.com";
    }

    @Test
    public void testRegisterUser() throws Exception {
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"" + username + "\", " +
                                "\"password\": \"" + password + "\", " +
                                "\"confirmPassword\": \"" + password + "\", " +
                                "\"email\": \"" + email + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully."));
    }

    @Test
    public void testLoginUserWithValidCredentials() throws Exception {
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"" + username + "\", " +
                                "\"password\": \"" + password + "\", " +
                                "\"confirmPassword\": \"" + password + "\", " +
                                "\"email\": \"" + email + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully."));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usernameOrEmail\": \"" + username + "\", " +
                                "\"password\": \"" + password + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.jwt").exists());
    }


    @Test
    public void testRegisterUserWithExistingUsername() throws Exception {
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"" + username + "\", " +
                                "\"password\": \"" + password + "\", " +
                                "\"confirmPassword\": \"" + password + "\", " +
                                "\"email\": \"" + email + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully."));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"" + username + "\", " +
                                "\"password\": \"" + password + "\", " +
                                "\"confirmPassword\": \"" + password + "\", " +
                                "\"email\": \"different_" + email + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists."));
    }

    @Test
    public void testRegisterUserWithExistingEmail() throws Exception {
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"" + username + "\", " +
                                "\"password\": \"" + password + "\", " +
                                "\"confirmPassword\": \"" + password + "\", " + // Dodano `confirmPassword`
                                "\"email\": \"" + email + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully."));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"different_" + username + "\", " +
                                "\"password\": \"" + password + "\", " +
                                "\"confirmPassword\": \"" + password + "\", " + // Dodano `confirmPassword`
                                "\"email\": \"" + email + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already exists."));
    }
}
