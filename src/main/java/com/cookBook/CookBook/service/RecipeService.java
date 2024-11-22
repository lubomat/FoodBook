package com.cookBook.CookBook.service;

import com.cookBook.CookBook.model.Category;
import com.cookBook.CookBook.model.Recipe;
import com.cookBook.CookBook.model.RecipeStep;
import com.cookBook.CookBook.model.User;
import com.cookBook.CookBook.repository.CategoryRepository;
import com.cookBook.CookBook.repository.RecipeRepository;
import com.cookBook.CookBook.repository.RecipeStepRepository;
import com.cookBook.CookBook.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private static final Logger logger = LoggerFactory.getLogger(RecipeService.class);

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final UserRepository userRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, CategoryRepository categoryRepository, RecipeStepRepository recipeStepRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.recipeStepRepository = recipeStepRepository;
        this.userRepository = userRepository;
    }

    public List<Recipe> getAllRecipes() {
        logger.info("Fetching all recipes.");
        return recipeRepository.findAll();
    }

    public List<Recipe> getRecipesByCategory(Long categoryId) {
        logger.info("Fetching recipes by category ID: {}", categoryId);
        return recipeRepository.findByCategoryId(categoryId);
    }

    public Optional<Recipe> getRecipeByName(String name) {
        logger.info("Fetching recipe by name: {}", name);
        return recipeRepository.findByName(name);
    }

    public Optional<Recipe> getRecipeBySlug(String slug) {
        logger.info("Looking for recipe with slug: {}", slug);
        Optional<Recipe> recipe = recipeRepository.findBySlug(slug);
        if (recipe.isPresent()) {
            logger.info("Recipe found with slug: {}", slug);
        } else {
            logger.warn("No recipe found with slug: {}", slug);
        }
        return recipe;
    }


    public Optional<Recipe> getRecipeById(Long id) {
        logger.info("Fetching recipe by ID: {}", id);
        return recipeRepository.findById(id);
    }

    public Recipe addRecipe(Recipe recipe, List<RecipeStep> steps) {
        logger.info("Attempting to add a new recipe: {}", recipe.getName());

        recipe.generateSlug();

        Optional<Recipe> existingRecipe = recipeRepository.findByName(recipe.getName());
        if (existingRecipe.isPresent()) {
            logger.error("Recipe with name {} already exists.", recipe.getName());
            throw new RuntimeException("Recipe named " + recipe.getName() + " already exists.");
        }

        Recipe savedRecipe = recipeRepository.save(recipe);
        logger.info("Recipe {} saved successfully with ID: {}", recipe.getName(), savedRecipe.getId());

        for (RecipeStep step : steps) {
            step.setRecipe(savedRecipe);
            recipeStepRepository.save(step);
            logger.info("Saved step {} for recipe {}", step.getStepNumber(), recipe.getName());
        }

        return savedRecipe;
    }

    public Recipe updateRecipe(Long id, Recipe updatedRecipe, List<RecipeStep> updatedSteps) {
        logger.info("Attempting to update recipe with ID: {}", id);

        return recipeRepository.findById(id)
                .map(recipe -> {
                    logger.info("Updating recipe: {}", recipe.getName());
                    recipe.setName(updatedRecipe.getName());
                    recipe.setIngredients(updatedRecipe.getIngredients());
                    recipe.setCategory(updatedRecipe.getCategory());
                    recipe.generateSlug();

                    logger.info("Deleting existing steps for recipe ID: {}", id);
                    recipeStepRepository.deleteAll(recipe.getSteps());

                    for (RecipeStep step : updatedSteps) {
                        step.setRecipe(recipe);
                        recipeStepRepository.save(step);
                        logger.info("Saved updated step {} for recipe {}", step.getStepNumber(), updatedRecipe.getName());
                    }

                    Recipe savedRecipe = recipeRepository.save(recipe);
                    logger.info("Recipe with ID {} updated successfully.", id);
                    return savedRecipe;
                })
                .orElseThrow(() -> {
                    logger.error("Recipe with ID {} not found.", id);
                    return new RuntimeException("Recipe not found");
                });
    }

    public void deleteRecipe(Long id) {
        logger.info("Attempting to delete recipe with ID: {}", id);
        recipeRepository.deleteById(id);
        logger.info("Recipe with ID {} deleted successfully.", id);
    }

    public Category getCategoryById(Long categoryId) {
        logger.info("Fetching category by ID: {}", categoryId);
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    logger.error("Category with ID {} not found.", categoryId);
                    return new RuntimeException("Category not found with id: " + categoryId);
                });
    }

    public List<Recipe> getRecipesByUser(String username) {
        logger.info("Fetching recipes for user: {}", username);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            logger.info("User {} found. Fetching recipes.", username);
            return recipeRepository.findByUserId(user.get().getId());
        } else {
            logger.error("User {} not found.", username);
            throw new RuntimeException("Nie znaleziono użytkownika: " + username);
        }
    }
}
