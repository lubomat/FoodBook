package com.cookBook.CookBook.service;

import com.cookBook.CookBook.model.Category;
import com.cookBook.CookBook.model.Recipe;
import com.cookBook.CookBook.model.RecipeStep;
import com.cookBook.CookBook.repository.CategoryRepository;
import com.cookBook.CookBook.repository.RecipeRepository;
import com.cookBook.CookBook.repository.RecipeStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final RecipeStepRepository recipeStepRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, CategoryRepository categoryRepository, RecipeStepRepository recipeStepRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.recipeStepRepository = recipeStepRepository;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public List<Recipe> getRecipesByCategory(Long categoryId) {
        return recipeRepository.findByCategoryId(categoryId);
    }

    public Optional<Recipe> getRecipeByName(String name) {
        return recipeRepository.findByName(name);
    }

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    public Recipe addRecipe(Recipe recipe, List<RecipeStep> steps) {
        Recipe savedRecipe = recipeRepository.save(recipe);
        for (RecipeStep step : steps) {
            step.setRecipe(savedRecipe);
            recipeStepRepository.save(step);
        }
        return savedRecipe;
    }

    public Recipe updateRecipe(Long id, Recipe updatedRecipe, List<RecipeStep> updatedSteps) {
        return recipeRepository.findById(id)
                .map(recipe -> {
                    recipe.setName(updatedRecipe.getName());
                    recipe.setIngredients(updatedRecipe.getIngredients());
                    recipe.setCategory(updatedRecipe.getCategory());

                    // Usuwamy stare kroki
                    recipeStepRepository.deleteAll(recipe.getSteps());

                    // Dodajemy zaktualizowane kroki
                    for (RecipeStep step : updatedSteps) {
                        step.setRecipe(recipe);
                        recipeStepRepository.save(step);
                    }

                    return recipeRepository.save(recipe);
                })
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
    }
}
