package com.cookBook.CookBook.controller;

import com.cookBook.CookBook.model.Recipe;
import com.cookBook.CookBook.model.RecipeStep;
import com.cookBook.CookBook.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/category/{categoryId}")
    public List<Recipe> getRecipesByCategory(@PathVariable Long categoryId) {
        return recipeService.getRecipesByCategory(categoryId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        return recipe.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{recipeName}")
    public ResponseEntity<Recipe> getRecipeByName(@PathVariable String recipeName) {
        Optional<Recipe> recipe = recipeService.getRecipeByName(recipeName);
        return recipe.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Secured("ROLE_USER")
    @PostMapping
    public Recipe addRecipe(@RequestParam("name") String name,
                            @RequestParam("ingredients") String ingredients,
                            @RequestParam("steps") List<String> steps,
                            @RequestParam("category") Long categoryId,
                            @RequestParam("image") MultipartFile image) throws IOException {

        String fileType = image.getContentType();
        if (!fileType.startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nieprawidłowy typ pliku. Dozwolone są tylko obrazy.");
        }

        long maxFileSize = 5 * 1024 * 1024; // 5 MB
        if (image.getSize() > maxFileSize) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rozmiar pliku przekracza dozwolone 5 MB.");
        }

        String uploadDir = "D:/obrazyfoodbook/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File uploadFile = new File(uploadDir, image.getOriginalFilename());
        image.transferTo(uploadFile);

        String imageUrl = "/uploads/" + image.getOriginalFilename();

        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setIngredients(ingredients);
        recipe.setImageUrl(imageUrl);
        recipe.setCategory(recipeService.getCategoryById(categoryId));

        List<RecipeStep> recipeSteps = createRecipeSteps(steps);

        return recipeService.addRecipe(recipe, recipeSteps);
    }

    // Prywatna metoda tworząca kroki przepisu
    private List<RecipeStep> createRecipeSteps(List<String> steps) {
        List<RecipeStep> recipeSteps = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            RecipeStep step = new RecipeStep();
            step.setStepNumber(i + 1);
            step.setDescription(steps.get(i));
            recipeSteps.add(step);
        }
        return recipeSteps;
    }

    @Secured("ROLE_USER")
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id,
                                               @RequestBody Recipe updatedRecipe,
                                               @RequestParam List<String> steps) {
        List<RecipeStep> recipeSteps = createRecipeSteps(steps);
        return ResponseEntity.ok(recipeService.updateRecipe(id, updatedRecipe, recipeSteps));
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
