package com.cookBook.CookBook.service;

import com.cookBook.CookBook.model.Category;
import com.cookBook.CookBook.model.Recipe;
import com.cookBook.CookBook.model.RecipeStep;
import com.cookBook.CookBook.repository.CategoryRepository;
import com.cookBook.CookBook.repository.RecipeRepository;
import com.cookBook.CookBook.repository.RecipeStepRepository;
import com.cookBook.CookBook.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private RecipeStepRepository recipeStepRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RecipeService recipeService;

    private Recipe sampleRecipe;
    private Category breakfastCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock category
        breakfastCategory = new Category();
        breakfastCategory.setId(1L);
        breakfastCategory.setName("Śniadanie");
        breakfastCategory.setSlug("breakfast");

        // Mock recipe
        sampleRecipe = new Recipe();
        sampleRecipe.setId(1L);
        sampleRecipe.setName("Jajecznica");
        sampleRecipe.setIngredients("Jajka, Masło");
        sampleRecipe.setSlug("jajecznica");
        sampleRecipe.setCategory(breakfastCategory);
    }

    @Test
    void testAddRecipe() {
        // Mock existing recipe lookup
        when(recipeRepository.findByName("Jajecznica")).thenReturn(Optional.empty());

        // Mock category lookup
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(breakfastCategory));

        // Mock save operation
        when(recipeRepository.save(any(Recipe.class))).thenReturn(sampleRecipe);

        // Mock saving steps
        List<RecipeStep> steps = new ArrayList<>();
        steps.add(new RecipeStep());

        Recipe addedRecipe = recipeService.addRecipe(sampleRecipe, steps);

        // Verify operations and assertions
        assertEquals("Jajecznica", addedRecipe.getName());
        assertEquals(breakfastCategory, addedRecipe.getCategory());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
        verify(recipeStepRepository, times(steps.size())).save(any(RecipeStep.class));
    }

    @Test
    void testGetRecipesByCategory() {
        // Mock recipes for category ID 1
        when(recipeRepository.findByCategoryId(1L)).thenReturn(List.of(sampleRecipe));

        // Fetch recipes by category
        List<Recipe> recipes = recipeService.getRecipesByCategory(1L);

        // Verify results and repository interaction
        assertEquals(1, recipes.size());
        assertEquals("Jajecznica", recipes.get(0).getName());
        verify(recipeRepository, times(1)).findByCategoryId(1L);
    }

    @Test
    void testAddRecipeWithDuplicateName() {
        // Mock existing recipe lookup
        when(recipeRepository.findByName("Jajecznica")).thenReturn(Optional.of(sampleRecipe));

        // Attempt to add duplicate recipe
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            recipeService.addRecipe(sampleRecipe, new ArrayList<>());
        });

        // Verify exception message
        assertEquals("Recipe named Jajecznica already exists.", exception.getMessage());
        verify(recipeRepository, never()).save(any(Recipe.class));
    }
}
