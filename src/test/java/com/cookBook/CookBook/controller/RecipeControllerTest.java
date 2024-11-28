package com.cookBook.CookBook.controller;

import com.cookBook.CookBook.model.Category;
import com.cookBook.CookBook.model.Recipe;
import com.cookBook.CookBook.service.CloudinaryService;
import com.cookBook.CookBook.service.RecipeService;
import com.cookBook.CookBook.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @Mock
    private UserService userService;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private RecipeController recipeController;

    private Recipe sampleRecipe;
    private Category breakfastCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        breakfastCategory = new Category();
        breakfastCategory.setId(1L);
        breakfastCategory.setName("Śniadanie");
        breakfastCategory.setSlug("breakfast");

        sampleRecipe = new Recipe();
        sampleRecipe.setId(1L);
        sampleRecipe.setName("Jajecznica");
        sampleRecipe.setIngredients("Jajka, Masło");
        sampleRecipe.setSlug("jajecznica");
        sampleRecipe.setCategory(breakfastCategory);
    }

    @Test
    void testAddRecipe() throws Exception {
        // Mocking user authentication
        when(authentication.getName()).thenReturn("testuser");

        // Mocking user lookup
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(new com.cookBook.CookBook.model.User()));

        // Mocking category lookup
        when(recipeService.getCategoryById(1L)).thenReturn(breakfastCategory);

        // Mocking Cloudinary upload
        String testImageUrl = "http://test.com/test.jpg";
        MockMultipartFile imageFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes());
        when(cloudinaryService.uploadFile(imageFile)).thenReturn(testImageUrl);

        // Mocking recipe save
        when(recipeService.addRecipe(any(Recipe.class), anyList())).thenReturn(sampleRecipe);

        // Adding recipe
        ResponseEntity<?> response = recipeController.addRecipe(
                "Jajecznica",
                "Jajka, Masło",
                List.of("Rozgrzać patelnię", "Dodać jajka", "Smażyć do gotowości"),
                1L,
                imageFile,
                authentication
        );

        // Assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Recipe addedRecipe = (Recipe) response.getBody();
        assertEquals("Jajecznica", addedRecipe.getName());
        verify(recipeService, times(1)).addRecipe(any(Recipe.class), anyList());
    }

    @Test
    void testGetRecipesByCategory() {
        // Mocking recipe service to return recipes in "Śniadanie" category
        when(recipeService.getRecipesByCategory(1L)).thenReturn(List.of(sampleRecipe));

        // Fetching recipes
        List<Recipe> recipes = recipeController.getRecipesByCategory(1L);

        // Assertions
        assertEquals(1, recipes.size());
        assertEquals("Jajecznica", recipes.get(0).getName());
        verify(recipeService, times(1)).getRecipesByCategory(1L);
    }
}
