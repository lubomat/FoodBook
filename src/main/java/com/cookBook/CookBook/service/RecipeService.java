package com.cookBook.CookBook.service;

import com.cookBook.CookBook.model.Recipe;
import com.cookBook.CookBook.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

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

    public Recipe addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Recipe updateRecipe(Long id, Recipe updatedRecipe) {
        return recipeRepository.findById(id)
                .map(recipe -> {
                    recipe.setName(updatedRecipe.getName());
                    recipe.setIngredients(updatedRecipe.getIngredients());
                    recipe.setSteps(updatedRecipe.getSteps());
                    recipe.setCategory(updatedRecipe.getCategory());
                    return recipeRepository.save(recipe);
                })
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }
}
