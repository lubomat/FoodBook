package com.cookBook.CookBook.controller;

import com.cookBook.CookBook.model.Comment;
import com.cookBook.CookBook.model.Recipe;
import com.cookBook.CookBook.model.RecipeStep;
import com.cookBook.CookBook.model.User;
import com.cookBook.CookBook.service.CloudinaryService;
import com.cookBook.CookBook.service.CommentService;
import com.cookBook.CookBook.service.RecipeService;
import com.cookBook.CookBook.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @Autowired
    private CloudinaryService cloudinaryService;

    private static final Logger logger = LogManager.getLogger(RecipeController.class);

    @GetMapping
    public List<Recipe> getAllRecipes() {
        logger.info("Fetching all recipes.");
        List<Recipe> recipes = recipeService.getAllRecipes();
        logger.info("Successfully fetched {} recipes.", recipes.size());
        return recipes;
    }

    @GetMapping("/category/{categoryId}")
    public List<Recipe> getRecipesByCategory(@PathVariable Long categoryId) {
        List<Recipe> recipes = recipeService.getRecipesByCategory(categoryId);
        logger.info("Successfully fetched {} recipes for category ID: {}", recipes.size(), categoryId);
        return recipes;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        logger.info("Fetching recipe by ID: {}", id);
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (recipe.isPresent()) {
            logger.info("Recipe found with ID: {}", id);
            return ResponseEntity.ok(recipe.get());
        } else {
            logger.warn("Recipe not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{recipeName}")
    public ResponseEntity<Recipe> getRecipeByName(@PathVariable String recipeName) {
        Optional<Recipe> recipe = recipeService.getRecipeByName(recipeName);
        if (recipe.isPresent()) {
            logger.info("Recipe found with name: {}", recipeName);
            return ResponseEntity.ok(recipe.get());
        } else {
            logger.warn("Recipe not found with name: {}", recipeName);
            return ResponseEntity.notFound().build();
        }
    }

    @Secured("ROLE_USER")
    @PostMapping
    public ResponseEntity<?> addRecipe(@RequestParam("name") String name,
                                       @RequestParam("ingredients") String ingredients,
                                       @RequestParam("steps") List<String> steps,
                                       @RequestParam("category") Long categoryId,
                                       @RequestParam("image") MultipartFile image,
                                       Authentication authentication) {
        logger.info("Received request to add a recipe with name: {}", name);

        if (recipeService.getRecipeByName(name).isPresent()) {
            logger.warn("Recipe with name '{}' already exists.", name);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A recipe with this name already exists.");
        }

        try {
            String fileType = image.getContentType();
            if (!fileType.startsWith("image/")) {
                logger.error("Invalid file type for recipe image: {}", fileType);
                return ResponseEntity.badRequest().body("Invalid file type. Only images are allowed.");
            }

            long maxFileSize = 5 * 1024 * 1024; // 5 MB
            if (image.getSize() > maxFileSize) {
                logger.error("File size exceeds limit for image: {} bytes.", image.getSize());
                return ResponseEntity.badRequest().body("The file size exceeds the allowed 5 MB.");
            }

            String imageUrl = cloudinaryService.uploadFile(image);
            logger.info("Image uploaded successfully to URL: {}", imageUrl);

            Recipe recipe = new Recipe();
            recipe.setName(name);
            recipe.setIngredients(ingredients);
            recipe.setImageUrl(imageUrl);
            recipe.setCategory(recipeService.getCategoryById(categoryId));

            User currentUser = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> {
                        logger.error("User not found: {}", authentication.getName());
                        return new RuntimeException("User not found.");
                    });

            recipe.setUser(currentUser);
            List<RecipeStep> recipeSteps = createRecipeSteps(steps);

            Recipe savedRecipe = recipeService.addRecipe(recipe, recipeSteps);
            logger.info("Recipe '{}' successfully added by user: {}", name, currentUser.getUsername());

            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
        } catch (IOException e) {
            logger.error("Error uploading image for recipe '{}': {}", name, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while uploading image.");
        }
    }

    private List<RecipeStep> createRecipeSteps(List<String> steps) {
        logger.debug("Creating recipe steps for {} steps.", steps.size());
        List<RecipeStep> recipeSteps = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            RecipeStep step = new RecipeStep();
            step.setStepNumber(i + 1);
            step.setDescription(steps.get(i));
            recipeSteps.add(step);
        }
        logger.debug("Successfully created {} recipe steps.", recipeSteps.size());
        return recipeSteps;
    }

    @Secured("ROLE_USER")
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id,
                                               @RequestBody Recipe updatedRecipe,
                                               @RequestParam List<String> steps) {
        logger.info("Received request to update recipe with ID: {}", id);
        List<RecipeStep> recipeSteps = createRecipeSteps(steps);
        Recipe updated = recipeService.updateRecipe(id, updatedRecipe, recipeSteps);
        logger.info("Recipe with ID: {} successfully updated.", id);
        return ResponseEntity.ok(updated);
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        logger.info("Received request to delete recipe with ID: {}", id);
        recipeService.deleteRecipe(id);
        logger.info("Recipe with ID: {} successfully deleted.", id);
        return ResponseEntity.noContent().build();
    }

    @Secured("ROLE_USER")
    @GetMapping("/my-recipes")
    public List<Recipe> getMyRecipes(Authentication authentication) {
        String username = authentication.getName();
        logger.info("Fetching recipes for user: {}", username);
        List<Recipe> recipes = recipeService.getRecipesByUser(username);
        logger.info("Successfully fetched {} recipes for user: {}", recipes.size(), username);
        return recipes;
    }
}
