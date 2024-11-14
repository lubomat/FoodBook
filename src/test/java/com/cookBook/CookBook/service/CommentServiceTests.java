package com.cookBook.CookBook.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentServiceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    @BeforeEach
    void setup() throws Exception {
        Map<String, String> loginPayload = new HashMap<>();
        loginPayload.put("usernameOrEmail", "test");
        loginPayload.put("password", "test1");

        String response = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginPayload)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonResponse = objectMapper.readTree(response);

        if (jsonResponse.get("jwt") == null) {
            throw new IllegalStateException("JWT token is missing in the response.");
        }

        jwtToken = jsonResponse.get("jwt").asText();

        if (jwtToken.isEmpty()) {
            throw new IllegalStateException("JWT token is empty.");
        }

        System.out.println("JWT Token obtained: " + jwtToken);
    }

    @Test
    @DisplayName("Adding comment")
    void testAddCommentWithStars() throws Exception {
        Map<String, Object> commentPayload = new HashMap<>();
        commentPayload.put("content", "Testowy komentarz");
        commentPayload.put("rating", 5);

        mockMvc.perform(post("/api/comments/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Testowy komentarz"))
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    @DisplayName("Adding comment with no rating - expected error")
    void testAddCommentWithoutStars() throws Exception {
        Map<String, Object> commentPayload = new HashMap<>();
        commentPayload.put("content", "Komentarz bez gwiazdek");

        mockMvc.perform(post("/api/comments/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentPayload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Adding a comment without a JWT token - expected authorization error")
    void testAddCommentWithoutToken() throws Exception {
        Map<String, Object> commentPayload = new HashMap<>();
        commentPayload.put("content", "Testowy komentarz");
        commentPayload.put("rating", 5);

        mockMvc.perform(post("/api/recipes/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentPayload)))
                .andExpect(status().isForbidden());
    }
}
