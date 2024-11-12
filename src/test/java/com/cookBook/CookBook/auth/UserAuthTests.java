package com.cookBook.CookBook.auth;

import com.cookBook.CookBook.model.User;
import com.cookBook.CookBook.security.AuthenticationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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
        int randomNum = (int)(Math.random() * (10000 - 10 + 1)) + 10;
        this.username = "test" + randomNum;
        this.password = "test123";
        this.email = "test" + randomNum + "@example.com";
    }

    @Test
    public void testRegisterUser() throws Exception {
        User testUser = new User();
        testUser.setUsername(username);
        testUser.setPassword(password);
        testUser.setEmail(email);

        mockMvc.perform(post("/register")
                        .contentType("application/json")
                        .content("{\"username\": \"" + username + "\", \"password\": \"" + password + "\", \"email\": \"" + email + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    public void testLoginUser() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType("application/json")
                        .content("{\"usernameOrEmail\": \"test\", \"password\": \"test1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    // LOGOUT
}
