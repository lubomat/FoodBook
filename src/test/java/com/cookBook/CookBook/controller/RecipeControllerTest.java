//package com.cookBook.CookBook.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.io.File;
//import java.io.FileInputStream;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class RecipeControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private String username;
//    private String password;
//    private String jwtToken;
//
//    @BeforeEach
//    public void setup() throws Exception {
//        // Generowanie unikalnego username i email dla testu rejestracji
//        this.username = "test";
//        this.password = "test1";
//
//        // Logowanie przed testem i pobranie tokenu JWT
//        String loginPayload = "{\"usernameOrEmail\": \"" + username + "\", \"password\": \"" + password + "\"}";
//        String response = mockMvc.perform(post("/login")
//                        .contentType("application/json")
//                        .content(loginPayload))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        // Ekstrakcja tokenu JWT z odpowiedzi
//        this.jwtToken = response.split(":")[1].split("\"")[1]; // Zakładając, że token jest w formacie: {"jwt":"token_value"}
//    }
//
//    @Test
//    public void testAddRecipeWithImage() throws Exception {
//        // Przygotowanie pliku obrazu (symulacja przesyłania pliku)
//        File imageFile = new File("src/test/resources/images/test.jpg"); // Ścieżka do testowego pliku obrazu
//        MockMultipartFile image = new MockMultipartFile("image", imageFile.getName(), "image/jpeg", new FileInputStream(imageFile));
//
//        // Dane przepisu
//        String recipeName = "Testowy przepis " + System.currentTimeMillis();  // Unikalna nazwa
//        String ingredients = "Woda, mąka, cukier";
//        String steps = "[\"Krok 1\", \"Krok 2\", \"Krok 3\"]";
//        Long categoryId = 1L;  // Zakładając, że kategoria o ID 1 istnieje w bazie
//
//        // Wykonanie żądania POST do dodania przepisu z tokenem JWT w nagłówku
//        mockMvc.perform(multipart("/api/recipes")
//                        .file(image)
//                        .param("name", recipeName)
//                        .param("ingredients", ingredients)
//                        .param("steps", steps)
//                        .param("category", categoryId.toString())
//                        .header("Authorization", "Bearer " + jwtToken) // Dodanie tokenu JWT do nagłówka
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isOk())  // Sprawdzamy, czy status odpowiedzi to 200 OK
//                .andExpect(jsonPath("$.name").value(recipeName)) // Sprawdzanie, czy nazwa przepisu jest poprawna
//                .andExpect(jsonPath("$.ingredients").value(ingredients)) // Sprawdzanie, czy składniki są poprawne
//                .andExpect(jsonPath("$.imageUrl").exists()) // Sprawdzanie, czy URL obrazu jest zwrócony
//                .andExpect(jsonPath("$.steps").isArray()) // Sprawdzanie, czy kroki są tablicą
//                .andExpect(jsonPath("$.steps.length()").value(3)); // Sprawdzanie, czy są 3 kroki
//    }
//
//    @Test
//    public void testGetRecipeSteps() throws Exception {
//        // Sprawdzanie kroków przepisu po jego dodaniu
//        mockMvc.perform(get("/api/recipe-steps")  // Adres endpointu dla kroków przepisu
//                        .header("Authorization", "Bearer " + jwtToken))  // Dodanie tokenu JWT do nagłówka
//                .andExpect(status().isOk())  // Sprawdzamy, czy status odpowiedzi to 200 OK
//                .andExpect(jsonPath("$[0].description").value("Krok 1"))  // Sprawdzamy, czy pierwszy krok jest poprawny
//                .andExpect(jsonPath("$[1].description").value("Krok 2"))  // Sprawdzamy, czy drugi krok jest poprawny
//                .andExpect(jsonPath("$[2].description").value("Krok 3"));  // Sprawdzamy, czy trzeci krok jest poprawny
//    }
//}

package com.cookBook.CookBook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String username;
    private String password;
    private String jwtToken;

    @BeforeEach
    public void setup() throws Exception {
        // Generowanie unikalnego username i email dla testu rejestracji
        this.username = "test";
        this.password = "test1";

        // Logowanie przed testem i pobranie tokenu JWT
        String loginPayload = "{\"usernameOrEmail\": \"" + username + "\", \"password\": \"" + password + "\"}";
        String response = mockMvc.perform(post("/login")
                        .contentType("application/json")
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Ekstrakcja tokenu JWT z odpowiedzi
        this.jwtToken = response.split(":")[1].split("\"")[1]; // Zakładając, że token jest w formacie: {"jwt":"token_value"}
    }

    @Test
    public void testAddRecipeWithImage() throws Exception {
        // Przygotowanie pliku obrazu (symulacja przesyłania pliku)
        File imageFile = new File("src/test/resources/images/test.jpg"); // Ścieżka do testowego pliku obrazu
        MockMultipartFile image = new MockMultipartFile("image", imageFile.getName(), "image/jpeg", new FileInputStream(imageFile));

        // Dane przepisu
        String recipeName = "Testowy przepis " + System.currentTimeMillis();  // Unikalna nazwa
        String ingredients = "Woda, mąka, cukier";
        String steps = "[\"Krok 1\", \"Krok 2\", \"Krok 3\"]";
        Long categoryId = 1L;  // Zakładając, że kategoria o ID 1 istnieje w bazie

        // Wykonanie żądania POST do dodania przepisu z tokenem JWT w nagłówku
        String response = mockMvc.perform(multipart("/api/recipes")
                        .file(image)
                        .param("name", recipeName)
                        .param("ingredients", ingredients)
                        .param("steps", steps)
                        .param("category", categoryId.toString())
                        .header("Authorization", "Bearer " + jwtToken) // Dodanie tokenu JWT do nagłówka
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())  // Sprawdzamy, czy status odpowiedzi to 200 OK
                .andExpect(jsonPath("$.name").value(recipeName)) // Sprawdzanie, czy nazwa przepisu jest poprawna
                .andExpect(jsonPath("$.ingredients").value(ingredients)) // Sprawdzanie, czy składniki są poprawne
                .andExpect(jsonPath("$.imageUrl").exists()) // Sprawdzanie, czy URL obrazu jest zwrócony
                .andExpect(jsonPath("$.steps").isArray()) // Sprawdzanie, czy kroki są tablicą
                .andExpect(jsonPath("$.steps.length()").value(3)) // Sprawdzanie, czy są 3 kroki
                .andReturn().getResponse().getContentAsString(); // Zapisujemy odpowiedź w celu wykorzystania ID przepisu

        // Parsowanie odpowiedzi JSON, aby wyciągnąć ID przepisu
        String recipeId = response.split(":")[1].split(",")[0].replace("\"", "").trim();

        // Sprawdzamy czy kroki przepisu zostały zapisane w tabeli recipe_steps
        mockMvc.perform(get("/api/recipe-steps")  // Endpoint do pobrania kroków przepisu
                        .header("Authorization", "Bearer " + jwtToken))  // Dodanie tokenu JWT do nagłówka
                .andExpect(status().isOk())  // Sprawdzamy, czy status odpowiedzi to 200 OK
                .andExpect(jsonPath("$[0].description").value("Krok 1"))  // Sprawdzamy, czy pierwszy krok jest poprawny
                .andExpect(jsonPath("$[1].description").value("Krok 2"))  // Sprawdzamy, czy drugi krok jest poprawny
                .andExpect(jsonPath("$[2].description").value("Krok 3"))  // Sprawdzamy, czy trzeci krok jest poprawny
                .andExpect(jsonPath("$[0].recipeId").value(recipeId))  // Sprawdzamy, czy recipe_id jest powiązane z przepisem
                .andExpect(jsonPath("$[1].recipeId").value(recipeId))  // Sprawdzamy, czy recipe_id jest powiązane z przepisem
                .andExpect(jsonPath("$[2].recipeId").value(recipeId));  // Sprawdzamy, czy recipe_id jest powiązane z przepisem
    }
}
