package com.cookBook.CookBook.controller;

import com.cookBook.CookBook.model.Recipe;
import com.cookBook.CookBook.model.RecipeStep;
import com.cookBook.CookBook.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // Zmieniona obsługa dodawania przepisu z krokami i plikiem obrazu
    @PostMapping
    public Recipe addRecipe(@RequestParam("name") String name,
                            @RequestParam("ingredients") String ingredients,
                            @RequestParam("steps") List<String> steps,  // Przyjmowanie listy kroków
                            @RequestParam("category") Long categoryId,
                            @RequestParam("image") MultipartFile image) throws IOException {

        // Sprawdzenie rozszerzenia pliku
        String fileType = image.getContentType();
        if (!fileType.startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nieprawidłowy typ pliku. Dozwolone są tylko obrazy.");
        }

        // Sprawdzenie rozmiaru pliku
        long maxFileSize = 5 * 1024 * 1024; // 5 MB
        if (image.getSize() > maxFileSize) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rozmiar pliku przekracza dozwolone 5 MB.");
        }

        // Zapisz obraz na serwerze (w folderze "D:/obrazyfoodbook")
        String uploadDir = "D:/obrazyfoodbook/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();  // Tworzy katalog, jeśli nie istnieje
        }

        File uploadFile = new File(uploadDir, image.getOriginalFilename());
        image.transferTo(uploadFile);

        // Ścieżka do zapisanego pliku
        String imageUrl = "/uploads/" + image.getOriginalFilename();

        // Tworzenie i zapisywanie przepisu
        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setIngredients(ingredients);
        recipe.setImageUrl(imageUrl);
        recipe.setCategory(recipeService.getCategoryById(categoryId));

        // Przetwarzanie kroków
        List<RecipeStep> recipeSteps = createRecipeSteps(steps);

        return recipeService.addRecipe(recipe, recipeSteps); // Zaktualizowane wywołanie, aby zapisać przepisy i kroki
    }


    // Funkcja do tworzenia listy kroków z listy Stringów
    private List<RecipeStep> createRecipeSteps(List<String> steps) {
        List<RecipeStep> recipeSteps = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            RecipeStep step = new RecipeStep();
            step.setStepNumber(i + 1);  // Ustawienie numeru kroku
            step.setDescription(steps.get(i));  // Opis kroku
            recipeSteps.add(step);
        }
        return recipeSteps;
    }

    // Zmieniona obsługa aktualizacji przepisu, z obsługą aktualizacji kroków
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id,
                                               @RequestBody Recipe updatedRecipe,
                                               @RequestParam List<String> steps) {
        // Przetwarzanie zaktualizowanych kroków
        List<RecipeStep> recipeSteps = createRecipeSteps(steps);

        return ResponseEntity.ok(recipeService.updateRecipe(id, updatedRecipe, recipeSteps));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
