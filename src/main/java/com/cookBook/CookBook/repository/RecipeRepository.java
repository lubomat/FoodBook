package com.cookBook.CookBook.repository;

import com.cookBook.CookBook.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByCategoryId(Long categoryId);
    Optional<Recipe> findByName(String name);

    Optional<Recipe> findBySlug(String slug);

    List<Recipe> findByUserId(Long userId);
}
